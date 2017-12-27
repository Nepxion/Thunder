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

@set PROJECT_NAME=thunder-test
@set PROJECT_LIST=thunder-framework,%PROJECT_NAME%

if exist %PROJECT_NAME%\target rmdir /s/q %PROJECT_NAME%\target

@rem 执行相关模块的Maven Install
call mvn clean install -DskipTests -pl %PROJECT_LIST% -am

pause