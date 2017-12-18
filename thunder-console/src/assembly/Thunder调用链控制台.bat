@echo on
@echo =============================================================
@echo $                                                           $
@echo $                    Thunder调用链控制台                    $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Technologies All Right Reserved                  $
@echo $  Copyright(C) 2015                                        $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Thunder调用链控制台
@color 0a

@set CLASSPATH=./conf/;./lib/*
@set PATH=

call:JAVA_HOME_CHECK

:JAVA_HOME_CHECK
if "%JAVA_HOME%"=="" goto ECHO_JAVA_HOME_CHECK_QUIT
	
@rem echo Found Java Home=%JAVA_HOME%
echo JAVA_HOME=%JAVA_HOME%
goto SET_CLASSPATH_AND_RUN

:ECHO_JAVA_HOME_CHECK_QUIT
@rem echo Please set JAVA_HOME
echo 请设置JAVA_HOME
goto QUIT

:SET_CLASSPATH_AND_RUN
"%JAVA_HOME%\bin\java" -Dfile.encoding=GBK -Ddefault.client.encoding=GBK -Duser.language=zh -Duser.region=CN -Djava.security.policy=java.policy -Djava.library.path=%PATH% -Xms128m -Xmx512m -classpath %CLASSPATH% com.nepxion.thunder.console.TraceLauncher

:QUIT
pause;