package app;

import openjava.ojc.CommandArguments;
import openjava.ojc.CustomCompiler;

import java.io.PrintStream;

public class PathGenerator {
    public static void main(String[] args) {
        System.err.println("OpenJava Compiler Version 1.1 " + "build 20031119");
        CommandArguments arguments;
        String[] filePath = new String[1];
        filePath[0] = "src/app/CheckTriangle.oj";
        try {
            arguments = new CommandArguments(filePath);
        } catch (Exception e) {
            showUsage();
            return;
        }
        new CustomCompiler(arguments).run();
    }

    private static void showUsage() {
        PrintStream o = System.err;
        o.println("Usage : ojc <options> <source files>");
        o.println("where <options> includes:");
        o.println(
                "  -verbose                 "
                        + "Enable verbose output                  ");
        o.println(
                "  -g=<number>              "
                        + "Specify debugging info level           ");
        o.println(
                "  -d=<directory>           "
                        + "Specify where to place generated files ");
        o.println(
                "  -compiler=<class>        "
                        + "Specify regular Java compiler          ");
        o.println(
                "  --default-meta=<file>    "
                        + "Specify separated meta-binding configurations");
        o.println(
                "  -calleroff               "
                        + "Turn off caller-side translations      ");
        o.println(
                "  -C=<argument>            "
                        + "Pass the argument to Java compiler     ");
        o.println(
                "  -J<argument>             "
                        + "Pass the argument to JVM               ");
    }
}
