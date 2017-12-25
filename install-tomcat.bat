@echo on
@echo =============================================================
@echo $                                                           $
@echo $                      Nepxion Thunder                      $
@echo $                                                           $
@echo $                                                           $
@echo $                                                           $
@echo $  Nepxion Technologies All Right Reserved                  $
@echo $  Copyright(C) 2017                                        $
@echo $                                                           $
@echo =============================================================
@echo.
@echo off

@title Nepxion Thunder
@color 0a

@echo Please ensure to config JAVA_HOME with Java 7
@set JAVA_HOME=E:\Tool\JDK-1.7.0
@echo Found JAVA_HOME=%JAVA_HOME%

@set PROJECT_NAME=thunder-test

@rem 删除target，有时候mvn会clean失败，需事先强制删除target
if exist %PROJECT_NAME%\target rmdir /s/q %PROJECT_NAME%\target

@rem 执行相关模块的install
call mvn clean install -DskipTests -pl thunder-framework,%PROJECT_NAME% -am

pause