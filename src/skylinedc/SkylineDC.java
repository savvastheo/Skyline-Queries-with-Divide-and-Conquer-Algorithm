package skylinedc;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * SkylineDC Project
 * @author Savvas Theofilou
 */
public class SkylineDC {
    /**
     * This method opens the input file, also checks if the file is not found.
     * @param path Name of the file (argument passed when program starts)
     * @return A Scanner object 
     */
    public static Scanner openFile(String path){
        Scanner inputFile=null;
        try{
            inputFile=new Scanner(new File(path)); //path=name of the file, e.g. input500000.txt
        }
        catch (Exception e1){
            try{
                inputFile=new Scanner(new File(path+".txt")); //this is in case user forgot ".txt" 
            }
            catch (Exception e2){
                System.out.println("There was an error while opening the file, exiting..");
                System.exit(1);
            }
        }
        return inputFile;
    }
    
    /**
     * This method reads the file and saves the values in an ArrayList of Point
     * @param inputFile A Scanner object (inputFile)
     * @return ArrayList of Point
     */
    public static ArrayList<Point> readFile(Scanner inputFile){
        int listSize;
        ArrayList <Point> pointsList=null;
        try{
            listSize=inputFile.nextInt();
            pointsList=new ArrayList<>(listSize);
            while (inputFile.hasNext()){
                pointsList.add(new Point(inputFile.nextShort(),inputFile.nextShort()));
            }
        }
        catch (Exception e){
            System.out.println("There was an error while reading the file, exiting..");
            System.exit(1);
        }
        return pointsList;
    }
    
    /**
     * This method finds the Skyline of the points and returns them in an ArrayList.
     * This method uses a divide and conquer algorithm. It divides the set of the points to left and right 
     * parts recursively, it finds the smallest y coordinate of the left part and removes every point of the
     * right part that has a y coordinate larger than that. Then it finds points with same x coordinate but
     * bigger y coordinate and removes them.
     * @param pointsList: the points stored in an ArrayList
     * @return an ArrayList containing the points of the Skyline
     */
    public static ArrayList<Point> findSkyline(ArrayList<Point> pointsList){
        //When the list of points contain less or equal to 1 points recursion stops.
        if (pointsList.size()<=1){ 
            return pointsList;
        }
        
        ArrayList<Point> leftSkylinePoints=new ArrayList<>(); //List for the left part
        ArrayList<Point> rightSkylinePoints=new ArrayList<>(); //List for the right part
        
        //Left part
        for (int i=0;i<pointsList.size()/2;i++){
            leftSkylinePoints.add(pointsList.get(i));
        }
        //Right part
        for (int i=pointsList.size()/2;i<pointsList.size();i++){
            rightSkylinePoints.add(pointsList.get(i));
        }
        
        leftSkylinePoints=findSkyline(leftSkylinePoints); //Divide left part
        rightSkylinePoints=findSkyline(rightSkylinePoints); //Divide right part
        
        //Finds smallest y coordinate of the left part. Complexity: O(n).
        int minY=1001;
        for (int i=0;i<leftSkylinePoints.size();i++){
            if (leftSkylinePoints.get(i).y<minY){
                minY=leftSkylinePoints.get(i).y;
            }
        }
        
        //Removes every point on the right part that has larger y coordinate than the smallest y coordinate of the left part. Complexity: O(n).
        int index=0;
        while (index<rightSkylinePoints.size()){
            if (rightSkylinePoints.get(index).y>=minY){
                rightSkylinePoints.remove(index);
            }
            else{
                index++;
            }
        }
        
        leftSkylinePoints.addAll(rightSkylinePoints); //Merging left part with right part
        
        //Check for points with same x coordinate but larger y coordinate and removes them. Complexity: O(n).
        index=0;
        while (index<leftSkylinePoints.size()-1){
            if (leftSkylinePoints.get(index).x==leftSkylinePoints.get(index+1).x){
                if (leftSkylinePoints.get(index).y>leftSkylinePoints.get(index+1).y){
                    leftSkylinePoints.remove(index);
                }
                else{
                    leftSkylinePoints.remove(index+1);
                }
            }
            else{
                index++;
            }
        }
        
        return leftSkylinePoints;
    }
    
    /**
     * Main method. 
     * @param args the name of the input file  
     */
    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        
        String path=args[0];
        ArrayList<Point> totalPoints;
        ArrayList<Point> skylinePoints=new ArrayList<>(100);
        
        totalPoints=readFile(openFile(path)); 
        
        //Creating a new comparator for Point Class so that we can sort the points based on their x coordinates
        Collections.sort(totalPoints, new Comparator<Point>(){
            @Override
            public int compare(Point o1, Point o2) {
                return Integer.compare(o1.x, o2.x);
            }
            
        });
        
        skylinePoints=findSkyline(totalPoints); 
        
        System.out.println("Skyline consists of "+skylinePoints.size()+" points:");
        for (int i=0;i<skylinePoints.size();i++){   
            System.out.println("Point "+(i+1)+": ("+skylinePoints.get(i).x+","+skylinePoints.get(i).y+")");
        }
        
         
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms.");
        
    }
}