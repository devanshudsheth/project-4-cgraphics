/*
Program modified: FractalGrammars

Classes used: Input.class,FractalGrammars.class
This program modifies the FractalGrammars.java in textbook to allow rounded corners such as those in Dragon curve

The program requires as arguments filename of Input file, named Dragon.txt
The program will draw the curve by each mouse click which corresponds to a level

Left click increase level, right click decreases

implement using

javac FractalGrammars.java
java FractalGrammars Dragon.txt

Program by
Devanshu Sheth
dds160030

*/

import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class FractalGrammars extends Frame
{  
//main method
public static void main(String[] args)
   {  
 //display the arguments  
         for (String s : args)
{
            System.out.println("arg -> " + s);
}       
//if no arguments are entered display error
      if (args.length == 0)
      {
          System.out.println("Use filename as program argument.");
      }
      //else execute
      else
      {
          new FractalGrammars(args[0]);
      }
   }
   //display left or right click to increase or decrease level
   FractalGrammars(String fileName)
   {  super("Click left or right mouse button to change the level");
      addWindowListener(new WindowAdapter()
         {@Override
         public void windowClosing(
                 WindowEvent e){System.exit(0);}});
      //set size of Frame
      setSize (800, 600);
      add("Center", new CvFractalGrammars(fileName));
      //show is deprecated, use setVisible
      setVisible(true);
   }
}
//canvas class
class CvFractalGrammars extends Canvas
{  String fileName, axiom, strF, strf, strX, strY;
   int maxX, maxY, level = 1;
   double xLast, yLast, dir, lastDir, rotation, dirStart, fxStart, fyStart,
      lengthFract, reductFact;

   void error(String str)
   {  System.out.println(str);
      System.exit(1);
   }

   CvFractalGrammars(String fileName)
   {  Input inp = new Input(fileName);
      if (inp.fails())
         error("Cannot open input file."); 
      axiom = inp.readString(); inp.skipLine();
      strF = inp.readString(); inp.skipLine();
      strf = inp.readString(); inp.skipLine();
      strX = inp.readString(); inp.skipLine();
      strY = inp.readString(); inp.skipLine();
      rotation = inp.readFloat(); inp.skipLine();
      dirStart = inp.readFloat(); inp.skipLine();
      fxStart = inp.readFloat(); inp.skipLine();
      fyStart = inp.readFloat(); inp.skipLine();
      lengthFract = inp.readFloat(); inp.skipLine();
      reductFact = inp.readFloat();
      if (inp.fails())
         error("Input file incorrect.");

      addMouseListener(new MouseAdapter()
      {@Override
  public void mousePressed(MouseEvent evt)
         {  if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            {  level--;       // Right mouse button decreases level
               if (level < 1)
                  level = 1;
            }
            else
               level++;     // Left mouse button increases level
            repaint();
         }
      });

}

Graphics g;

//conversion from logical to device coordinates
int iX(double x){return (int)Math.round(x);}
int iY(double y){return (int)Math.round(maxY-y);}


//method to draw
void drawTo(Graphics g, double dx, double dy)
{  g.drawLine(iX(xLast), iY(yLast), iX(xLast + dx) ,iY(yLast + dy));
   xLast = xLast + dx;
   yLast = yLast + dy;
}

//method to move
void moveTo(Graphics g, double x, double y)
{  xLast = x;
   yLast = y;
} 

//override the paint method
@Override
public void paint(Graphics g)
{  Dimension d = getSize();
   maxX = d.width - 1;
   maxY = d.height - 1;
   xLast = fxStart * maxX;
   yLast = fyStart * maxY;
   dir = dirStart; // Initial direction in degrees
   
   //last direction so it can in fed as beginning of next
   lastDir = dirStart;
   String instructions = axiom;
   double finalLen = lengthFract * maxY;

   // To get final instruction string we need previous and next
   //iterate through the levels
   for(int k=0;k<level;k++)
   {
      String newInstructions = "";
      for(int j=0;j<instructions.length();j++)
      {
         char c = instructions.charAt(j);
         switch(c)
         {
            case 'F':
               newInstructions += strF;
               break;
            case 'f':
               newInstructions += strf;
               break;
            case 'X':
               newInstructions += strX;
               break;
            case 'Y':
               newInstructions += strY;
               break;
            default:
               newInstructions += c;
               break;
         }
      }
      instructions = newInstructions;
      finalLen *= lengthFract;
   }
   // Removes invalid instructions
   instructions = instructions.replaceAll("[^Ff\\+\\-\\]\\[]", "");
   
   // Removes angles that cancel each other
   instructions = instructions.replaceAll("\\+\\-|\\-\\+", "");

   turtleGraphics(g, instructions, level, finalLen);
}

public final double constant_fraction = .3;

// Modified to not be recursive
public void turtleGraphics(Graphics g, String instruction,int depth, double len)
{  double xMark=0, yMark=0, dirMark=0; 
   double toradians, dx, dy;
   for (int i=0;i<instruction.length();i++)
   {  char ch = instruction.charAt(i),
         nextCh = (instruction.length() > i+1) ? instruction.charAt(i+1) : '_',
         prevCh = (i > 0) ? instruction.charAt(i-1) : '_';
      switch(ch)
      {
      case 'F': 
         //draw and move forward
         //as given in hints and solutions
         toradians = Math.PI/180 * dir; // Convert to radians
         dx = len * Math.cos(toradians);
         dy = len * Math.sin(toradians);
         if((nextCh != '_' && (nextCh == '+' || nextCh == '-')) ||(prevCh != '_' && (prevCh == '+' || prevCh == '-')))
            {
               drawTo(g, dx*(1-constant_fraction), dy*(1-constant_fraction));
            }
            else
            {
               drawTo(g, dx, dy);   
            }
             
         break;
      case 'f':
         //move forward, do not draw
         toradians = Math.PI/180 * dir; // Convert to radians
         dx = len * Math.cos(toradians);
         dy = len * Math.sin(toradians);
         moveTo(g, xLast + dx, yLast + dy);
         break;
      case '+': 
         if((rotation == 90 || rotation == -90) && prevCh != '_' && prevCh == 'F' && nextCh != '_' && nextCh == 'F')
         {
            double cornerLen = len * Math.sqrt(constant_fraction*constant_fraction + constant_fraction*constant_fraction);
            toradians = Math.PI/180 * ((dir-rotation/2) % 360); // Convert to radians
            dx = cornerLen * Math.cos(toradians);
            dy = cornerLen * Math.sin(toradians);
            drawTo(g, dx, dy);
         }
         dir -= rotation; 
         break;
      case '-': 
          // Turn left
         if((rotation == 90 || rotation == -90) && prevCh != '_' && prevCh == 'F' && nextCh != '_' && nextCh == 'F')
         {
            double cornerLen = len * Math.sqrt(constant_fraction*constant_fraction + constant_fraction*constant_fraction);
            toradians = Math.PI/180 * ((dir+rotation/2) % 360); // Convert to radians
            dx = cornerLen * Math.cos(toradians);
            dy = cornerLen * Math.sin(toradians);
            drawTo(g, dx, dy);
         }
         dir += rotation; break;
      case '[': 
           // Save position and direction
         xMark = xLast; yMark = yLast; dirMark = dir; break;
      case ']': 
           // move back to previous saved position and direction
         xLast = xMark; yLast = yMark; dir = dirMark; break;
      }
    }
  }
}


//as given in textbook
class Input
{  private PushbackInputStream pbis;
   private boolean ok = true;
   private boolean eoFile = false;

   Input(){pbis = new PushbackInputStream(System.in);}

   Input(String fileName)
   {  try
      {   InputStream is = new FileInputStream(fileName);
          pbis = new PushbackInputStream(is);
      }
      catch(IOException ioe){ok = false;}
   }

   int readInt()
   {  boolean neg = false;
      char ch;
      do {
          ch = readChar();
      }while (Character.isWhitespace(ch));
      if (ch == '-')
      {
          neg = true; ch = readChar();
      }
      if (!Character.isDigit(ch))
      {  
         pushBack(ch);
         ok = false;
         return 0;
      }
      int x = ch - '0';
      for (;;)
      {  ch = readChar();
         if (!Character.isDigit(ch)){pushBack(ch); break;}
         x = 10 * x + (ch -  '0');
      }
      return (neg ? -x : x);
    }
float readFloat()
{  char ch;
   int nDec = -1;
   boolean neg = false;
   do
   {  ch = readChar();
   }   while (Character.isWhitespace(ch));
   if (ch == '-'){neg = true; ch = readChar();}
   if (ch == '.'){nDec = 1; ch = readChar();}
   if (!Character.isDigit(ch)){ok = false; pushBack(ch); return 0;}
   float x = ch - '0';
   for (;;)
   {  ch = readChar();
      if (Character.isDigit(ch))
      {  x = 10 * x + (ch - '0');
         if (nDec >= 0) nDec++;
      }
      else
      if (ch == '.' && nDec == -1) nDec = 0;
      else break;
   }
   while (nDec > 0){x *= 0.1; nDec--;}
   if (ch == 'e'  ||   ch == 'E')
   {  int exp = readInt();
      if (!fails())
      {   while (exp < 0){x *= 0.1; exp++;}
          while (exp > 0){x *= 10; exp--;}
      }
   }
   else pushBack(ch);
   return (neg ? -x : x);
}

char readChar()
{  int ch=0;
   try
   {   ch = pbis.read();
       if (ch == -1) {eoFile = true; ok = false;}
   }
   catch(IOException ioe){ok = false;}
   return (char)ch;
}

   String readString()    // Read first string between quotes (").
   {  String str = " ";
      char ch;
      do ch = readChar(); while (!(eof()  || ch == '"'));
                                                   // Initial quote
      for (;;)
      {   ch = readChar();
         if (eof()  ||   ch == '"') // Final quote (end of string)
            break;
         str += ch;
      }
      return str;
   }

   void skipLine()   // Skip rest of line
   {  char ch;
      do ch = readChar(); while (!(eof()  || ch == '\n'));
   }

   boolean fails(){return !ok;}
   boolean eof(){return eoFile;}
   void clear(){ok = true;}

   void close()
   {  if (pbis != null)
      try {pbis.close();}catch(IOException ioe){ok = false;}
   }

   void pushBack(char ch)
   {  try {pbis.unread(ch);}catch(IOException ioe){}
   }
}