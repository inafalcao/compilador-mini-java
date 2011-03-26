package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import syntaxtree.Program;
import visitor.*;

public final class MiniJava {
        private MiniJava() {
        }

        public static void main(String args[]) {
                System.out.println("\n--- Mini Java Parser ---");

                if (args.length == 0) {
                        System.out.println("Reading from standard input . . .");
                        compile(System.in);
                } else if (args.length == 1) {
                        File file = new File(args[0]);
                        try {
                                FileInputStream fileInputStream = new java.io.FileInputStream(file);
                                System.out.println("Reading from file " + file.getName() + " from " + file.getAbsolutePath() + ".");
                                compile(fileInputStream);
                        } catch (FileNotFoundException e) {
                                System.out.println("File " + file.getAbsolutePath() + " not found.");
                                return;
                        }
                } else {
                        System.out.println("Usage is one of:");
                        System.out.println("         java mjc.parser.MiniJavaCompiler < inputfile");
                        System.out.println("OR");
                        System.out.println("         java mjc.parser.MiniJavaCompiler inputfile");
                        return;
                }
        }

        public static void compile(InputStream inputStream) {
                final MiniJavaParser miniJavaParser = new MiniJavaParser(inputStream);
                final Program root;
                try {
                        root = miniJavaParser.Program();
                        root.accept(new PrettyPrintVisitor());
                        System.out.println("MiniJava program parsed successfully.");
                } catch (ParseException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Encountered errors during parse.");
                        return;
                }
                System.out.println("MiniJava program compiled successfully");
        }
}
