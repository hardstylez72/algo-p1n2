package helloword;

/******************************************************************************
 *  Compilation:  javac HelloWorld.java
 *  Execution:    java HelloWorld
 *
 *  Prints "Hello, World". By tradition, this is everyone's first program.
 *
 *  % java HelloWorld
 *  Hello, World
 *
 *  These 17 lines of text are comments. They are not part of the program;
 *  they serve to remind us about its properties. The first two lines tell
 *  us what to type to compile and test the program. The next line describes
 *  the purpose of the program. The next few lines give a sample execution
 *  of the program and the resulting output. We will always include such
 *  lines in our programs and encourage you to do the same.
 *
 ******************************************************************************/

public class HelloGoodbye {

    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        if (args.length >= 2) {
           String name1 = args[0];
           String name2 = args[1];
          System.out.println("Hello " + name1 + " and " + name2);
          System.out.println("Goodbye " + name2 + " and " + name1);
        }

    }

}
