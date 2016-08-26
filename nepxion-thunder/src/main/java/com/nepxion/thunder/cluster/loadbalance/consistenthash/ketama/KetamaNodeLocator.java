/**
 * Copyright (C) 2006-2009 Dustin Sallings
 * Copyright (C) 2009-2011 Couchbase, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALING
 * IN THE SOFTWARE.
 */

package com.nepxion.thunder.cluster.loadbalance.consistenthash.ketama;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Ketama is an implementation of a consistent hashing algorithm, meaning you can add or remove servers from the memcached pool without causing a complete remap of all keys.
   Here’s how it works:
 * Take your list of servers (eg: 1.2.3.4:11211, 5.6.7.8:11211, 9.8.7.6:11211)
 * Hash each server string to several (100-200) unsigned ints
 * Conceptually, these numbers are placed on a circle called the continuum. (imagine a clock face that goes from 0 to 2^32)
 * Each number links to the server it was hashed from, so servers appear at several points on the continuum, by each of the numbers they hashed to.
 * To map a key->server, hash your key to a single unsigned int, and find the next biggest number on the continuum. The server linked to that number is the correct server for that key.
 * If you hash your key to a value near 2^32 and there are no points on the continuum greater than your hash, return the first server in the continuum.
   If you then add or remove a server from the list, only a small proportion of keys end up mapping to different servers.
*/

/**
 * This is an implementation of the Ketama consistent hash strategy from
 * last.fm. This implementation may not be compatible with libketama as hashing
 * is considered separate from node location.
 *
 * Note that this implementation does not currently supported weighted nodes.
 *
 * @see <a href="http://www.last.fm/user/RJ/journal/2007/04/10/392555/">RJ's
 *      blog post</a>
 */
public final class KetamaNodeLocator<T> implements NodeLocator<T> {

    private volatile TreeMap<Long, T> ketamaNodes;
    private List<T> allNodes;

    private final HashAlgorithm hashAlg;
    private final KetamaNodeLocatorConfiguration<T> config;

    /**
     * Create a new KetamaNodeLocator using specified nodes and the specifed
     * hash algorithm.
     *
     * @param nodes
     *            The List of nodes to use in the Ketama consistent hash
     *            continuum
     * @param alg
     *            The hash algorithm to use when choosing a node in the Ketama
     *            consistent hash continuum
     */
    public KetamaNodeLocator(List<T> nodes, HashAlgorithm alg) {
        this(nodes, alg, new DefaultKetamaNodeLocatorConfiguration<T>());
    }

    /**
     * Create a new KetamaNodeLocator using specified nodes and the specifed
     * hash algorithm and configuration.
     *
     * @param nodes
     *            The List of nodes to use in the Ketama consistent hash
     *            continuum
     * @param alg
     *            The hash algorithm to use when choosing a node in the Ketama
     *            consistent hash continuum
     * @param conf
     */
    public KetamaNodeLocator(List<T> nodes, HashAlgorithm alg, KetamaNodeLocatorConfiguration<T> conf) {
        super();

        hashAlg = alg;
        config = conf;
        setKetamaNodes(nodes);
    }

    public List<T> getAll() {
        return allNodes;
    }

    public T getPrimary(final String k) {
        T rv = getNodeForKey(hashAlg.hash(k));
        assert rv != null : "Found no node for key " + k;
        return rv;
    }

    long getMaxKey() {
        return getKetamaNodes().lastKey();
    }

    // 计算流程
    // 处理完正式节点在环上的分布后，可以开始key在环上寻找节点。
    // 在环上顺时针查找，如果找到某个节点，就返回那个节点;如果没有找到，则取整个环的第一个节点。 
    T getNodeForKey(long hash) {
        final T rv;
        if (!ketamaNodes.containsKey(hash)) {
            // Java 1.6 adds a ceilingKey method, but I'm still stuck in 1.5
            // in a lot of places, so I'm doing this myself.
            // 得到大于当前key的那个子Map，然后从中取出第一个key，就是大于且离它最近的那个key
            /*SortedMap<Long, T> tailMap = getKetamaNodes().tailMap(hash);
            if (tailMap.isEmpty()) {
                hash = getKetamaNodes().firstKey();
            } else {
                hash = tailMap.firstKey();
            }*/
            
            // 在JDK1.6中，ceilingKey方法可以返回大于且离它最近的那个key 
            // For JDK1.6 version  
            Long newHash = getKetamaNodes().ceilingKey(hash);  
            if (newHash == null) {
                hash = getKetamaNodes().firstKey();
            } else {
                hash = newHash.longValue();
            }
        }
        // 如果找到这个节点，直接取节点，返回 
        rv = getKetamaNodes().get(hash);
        return rv;
    }

    public Iterator<T> getSequence(String k) {
        // Seven searches gives us a 1 in 2^7 chance of hitting the
        // same dead node all of the time.
        return new KetamaIterator<T>(k, 7, getKetamaNodes(), hashAlg);
    }

    @Override
    public void updateLocator(List<T> nodes) {
        setKetamaNodes(nodes);
    }

    /**
     * @return the ketamaNodes
     */
    protected TreeMap<Long, T> getKetamaNodes() {
        return ketamaNodes;
    }

    /**
     * Setup the KetamaNodeLocator with the list of nodes it should use.
     *
     * @param nodes
     *            a List of MemcachedNodes for this KetamaNodeLocator to use in
     *            its continuum
     */
    // 计算流程
    // 四个虚拟结点为一组，以getKeyForNode方法得到这组虚拟节点的name，
    // Md5编码后，每个虚拟节点对应Md5码16个字节中的4个，组成一个long型数值，做为这个虚拟结点在环中的惟一key。
    // 用Long类型，是因为其实现了Comparator接口。
    protected void setKetamaNodes(List<T> nodes) {
        allNodes = nodes;
        TreeMap<Long, T> newNodeMap = new TreeMap<Long, T>();
        // 虚拟节点数
        int numReps = config.getNodeRepetitions();
        for (T node : nodes) {
            // Ketama does some special work with md5 where it reuses chunks.
            if (hashAlg == DefaultHashAlgorithm.KETAMA_HASH) {
                // 每四个虚拟节点为一组
                for (int i = 0; i < numReps / 4; i++) {
                    // getKeyForNode方法为这组虚拟结点得到惟一名称 
                    byte[] digest = DefaultHashAlgorithm.computeMd5(config.getKeyForNode(node, i));
                    // Md5是一个16字节长度的数组，将16字节的数组每四个字节一组，分别对应一个虚拟结点，这就是为什么上面把虚拟结点四个划分一组的原因
                    for (int h = 0; h < 4; h++) {
                        // 对于每四个字节，组成一个long值数值，做为这个虚拟节点的在环中的惟一key 
                        Long k = ((long) (digest[3 + h * 4] & 0xFF) << 24)
                                | ((long) (digest[2 + h * 4] & 0xFF) << 16)
                                | ((long) (digest[1 + h * 4] & 0xFF) << 8)
                                | (digest[h * 4] & 0xFF);
                        newNodeMap.put(k, node);
                    }
                }
            } else {
                for (int i = 0; i < numReps; i++) {
                    newNodeMap.put(hashAlg.hash(config.getKeyForNode(node, i)), node);
                }
            }
        }
        assert newNodeMap.size() == numReps * nodes.size();
        ketamaNodes = newNodeMap;
    }
}