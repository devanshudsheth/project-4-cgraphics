/*

Stairs.java

Program written by Devanshu Sheth
dds160030

This program creates a file for e.g. stairs.dat (named according to input).
The file contains a list of all the vertices and the faces and their coordinates as a part of the 3D coordinate system.
This file when put through the HLines.java, Painter.java or Zbuf.java will give their corresponging outputs.


This program is a combination of Beams.java and Cylinder.java
Most of the code is from the textbook and above given programs

implement using command prompt (run as admin)

javac Stairs.java
java Stairs 25 15 stairs.dat

Open Hlines and execute.
Open stairs.dat

*/

import java.io.*;

public class Stairs
{
    //main method
	public static void main(String[] args)throws IOException
   {  
   if (args.length != 3)
      {  
         //display error if proper arguments are not entered 
         System.out.println("Supply n (> 0), alpha (in degrees)\n" + "and a filename as program arguments.\n"); 
         System.exit(1);
      }
      int n = 0 ; 
      double a = 6.0, alphaDeg = 0;
      //check the validity of the arguments entered
      try
      {  
         n = Integer.valueOf(args[0]).intValue();
       //  a = Double.valueOf(args[1]).doubleValue();
         //degree of rotation of the stairs
         alphaDeg = Double.valueOf(args[1]).doubleValue();
      
         if (n <= 0 || a < 0.5)throw new NumberFormatException();
      }
      catch (NumberFormatException e)
      {  System.out.println("n must be an integer > 0");
         System.out.println("alpha must be a real number");

         System.exit(1);
      }
      //call the constructor of makeStairs
      new makeStairs(n, a, alphaDeg * Math.PI / 180, args[2]);
      System.out.println("File has been created");
    }
}
//Point3D.java: Representation of a point in 3D space.
class Point3D
{  float x, y, z;
   Point3D(double x, double y, double z)
   {  this.x = (float)x;
      this.y = (float)y;
      this.z = (float)z;
   }
}
//class where the stairs are made
class makeStairs
{  
//declare FileWriter Object    
FileWriter fw;

//assigned the device coordinates needed
int n2 , n3 , n4 , m, var1=0;
float var2 =0;

//constructor for makeStairs
makeStairs(int n, double a, double alpha, String fileName)
      throws IOException
   {  
      int n2 = 2 * n, n3 = 3 * n, n4 = 4 * n; 
      fw = new FileWriter(fileName);
      Point3D[] P = new Point3D[9];
      double b = a - 2.8;
      
     //generate vertices for the cylinder
      P[1] = new Point3D(a, -a, 0);
      P[2] = new Point3D(a,  a, 0);
      P[3] = new Point3D(b,  a, 0);
      P[4] = new Point3D(b, -a, 0);
      P[5] = new Point3D(a, -a, 0.2);
      P[6] = new Point3D(a,  a, 0.2);
      P[7] = new Point3D(b,  a, 0.2);
      P[8] = new Point3D(b, -a, 0.2);
    
      //generate vertices for the beams 
      for (int k=0; k<n; k++)
      {  
         double phi = k * alpha, cosPhi = Math.cos(phi), sinPhi = Math.sin(phi),x,y;
         float x1=0,y1=0,z1=0, x1new=0,y1new=0,newz=0;
		
    
         for (int i=1; i<=8; i++)
         {   
        	  x = P[i].x; y = P[i].y; 
          
        	  x1 = (float)(x * cosPhi - y * sinPhi);
               y1 = (float)(x * sinPhi + y * cosPhi);
               z1 = (float)((P[i].z + k));
              
               if(i==1||i==2)
             	    {
                        
                    x1new =x1+x1new;
                    y1new=y1+y1new;newz=(z1+newz);}
               var1++;  
         fw.write((var1) + " " + x1 + " " + y1 + " " + z1 + "\r\n");
       
      }
       //generate vertices for the railing
         var1++;
         fw.write((var1) + " " + x1new/2 + " " + y1new/2 + " " + newz/2 +
                 "\r\n");
         var1++;
                 if(var1==10)
                 {var2=(x1new/2);
                 var2+=2;
                	 fw.write((var1) + " " + x1new/2 + " " + y1new/2 + " " + (var2) +
                         "\r\n");}
                 else{
                	 var2+=1;
                	 fw.write((var1) + " " + x1new/2 + " " + y1new/2 + " " + (var2) +
                         "\r\n");}
    }
      var1++;
     //generate vertices for cylinder 
      makeCylinder(n, (float) (0.326), 0);
      fw.write("Faces:\r\n");
      //generate faces for cylinder
      drawFaces(n,n2,n3,n4,0, (float) (0.326));
      
   
   //generate faces for the beam 
    for (int k=0; k<n; k++)
    {  // Beam k again:
       int var3 = 10 * k;
       face(var3, 1, 2, 6, 5);
       face(var3, 4, 8, 7, 3);
       face(var3, 5, 6, 7, 8);
       face(var3, 1, 4, 3, 2);
       face(var3, 2, 3, 7, 6);
       face(var3, 1, 5, 8, 4);

    }
    //generate faces for railing
    int xrail=9,yrail=10;
    fw.write(xrail+" "+   
            +(yrail)+".\r\n");
    for(int f=1;f<n;f++){
    	xrail++;yrail+=10;
    	fw.write(xrail+" "+
                +(yrail)+".\r\n");
    	xrail+=9;
    	fw.write(xrail+" "+
                +(yrail)+".\r\n");
    
    }
    fw.close();
  }
  
void face(int m, int a, int b, int c, int d)throws IOException
  {  a += m; b += m; c += m; d += m;
     fw.write(a + " " + b + " " + c + " " + d + ".\r\n");
  }

//Method to generate vertices for cylinder
//as given in textbook
  void makeCylinder(int n, float rOuter, float rInner)
		   throws IOException
		{  
		   double delta=2* Math.PI / n;
		   double heightpm = (n/25)*15;
		   for (int i=1; i<=n; i++)
		   { 
			   double alpha=i* delta,
		        cosa = Math.cos(alpha), sina = Math.sin(alpha);
		     
		      double r = rOuter ;
		       if (r > 0)
		       for (int bottom=0; bottom<2; bottom++)
		       { int k = (2 * 0 +  bottom)   * n + i;
		          // Vertex numbers for i = 1:
		          // Top:      1 (outer) 2n+1 (inner)
		          // Bottom: n+1 (outer) 3n+1 (inner)
		          wi(k+var1); // w = write, i = int, r = real
		          wr(r * cosa*10); wr(r * sina*10); // x and y
		          if((k+var1>var1+25)&& (k+var1<var1+51))
		        	  wi(0);
		          else
		        	  wi((int) ((2 - bottom)*heightpm)); // bottom: z = 0; top: z = 1
		          fw.write("\r\n");
		       }
		     }
		   }
  //Method to generate faces for cylinder
  //taken from textbook
		private void drawFaces(int n, int n2, int n3,int n4, float rInner, float rOuter) 
		{
			try { 
					for (int i=1; i<=n; i++)
                                        {
                                            wi(i+var1);
                                        }
					fw.write(".\r\n");
					// Bottom boundary face:
					for (int i=n2; i>=n+1; i--) wi(i+var1);
				    fw.write(".\r\n");
				    // Vertical, rectangular faces:
				    for (int i=1; i<=n; i++)
				    { 
				    	int j = i % n + 1;
				    	// Outer rectangle:
				    	wi(j+var1); wi(i+var1); wi(i + n+var1); wi(j + n+var1);
                                        fw.write(".\r\n");
				    }
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
		}
		void wi(int x)
		   throws IOException
		{
			fw.write(" " + String.valueOf(x));
		}

		void wr(double x)
		   throws IOException
		{ 
			if (Math.abs(x) < 1e-9) x = 0;
		  fw.write(" " + String.valueOf((float)x));
		 }
	}

