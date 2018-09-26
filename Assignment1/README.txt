This program was tested in room 5029 on the XCOM machine.


Additional Info

I ran my program by creating 2 batch files and running them in the Sorvari-Nik-a1 folder.

Compile.bat contains:
	javac a1/*.java
Run.bat contains:
	java -Dsun.java2d.d3d=false -Djogl.debug.DebugGL -Djogl.debug.TraceGL a1.Starter

this run command is the ONLY way I could run my program.