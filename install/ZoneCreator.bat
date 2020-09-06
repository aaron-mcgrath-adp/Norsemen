setlocal ENABLEDELAYEDEXPANSION

set CLASSPATH=.
set HOME=.
set JAVA_HOME=java\zulu-11\bin

set CLASSPATH=%CLASSPATH%;%HOME%\lib\*;%HOME%\config;%HOME%\config\zones;%HOME%\config\images;%HOME%\config\images\Player\walk-right-armed;%HOME%\config\images\Player\walk-left-armed;%HOME%\config\maps;%HOME%\config\sounds;%HOME%\config\images\GoblinRun


%JAVA_HOME%\java -cp %CLASSPATH% -Xmx2048m -Xdebug -Dlog4j.skipJansi=false -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1045 amc.levelcreator.Main
