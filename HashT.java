/*
--------------------------------------
Estructura de Datos, LuJu 8:30
Proyeto Final
--------------------------------------
Emanuel Estrada Larios - A01633605
Sebastian Cedeno Gonzalez
--------------------------------------
*/

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.*;
import javax.imageio.*;
import java.net.*;

public class HashT{

  private int m, nodeNum;
  private Key[] hashTable;

  /////////////////////////////////////////////////////////

  /* Constructor de FastLinearProbe:
      - Incializa table con tamano de numero primo
      - Agrega elemento dummy en indice 0
  */

  public HashT(){
    this.m = 65;
    this.hashTable = new Key[this.m];
    this.nodeNum = 0;
  }

  /////////////////////////////////////////////////////////

  /* Funcion hash (open adressing):
      - Regresa el indice calculado para la llave correspondiente
   */

  private int hash(Key key){
    if(key.k == 640)
      return 0;
    else
      return key.k % this.m;
  }

  ///////////////////////////////////////////////////////

  public int insertElement(String path){

    Key key = this.imgKeyGen(path);
    int j = hash(key);

    if(this.hashTable[j] == null){
      this.hashTable[j] = key;
      //System.out.println("Insertado en: " + j);
      return j;
    }
    else{
      Key nextNode = this.hashTable[j];
      this.hashTable[j] = key;
      this.hashTable[j].next = nextNode;
      //System.out.println("Insertado en: " + j);
      return j;
    }

  }

  ///////////////////////////////////////////////////////

  public int searchElement(String path){

    Key key = this.imgKeyGen(path);
    int j = hash(key);

    if(this.hashTable[j] != null){
      if(this.hashTable[j].pxID.equals(key.pxID)){
        this.nodeNum = 0;
        return j;
      }
      else{
        Key start = this.hashTable[j].next;
        this.nodeNum = 1;
        while(start != null){
          if(start.pxID.equals(key.pxID))
            return j;
          else{
            start = start.next;
            this.nodeNum++;
          }
        }
        return -1 * j;
      }
    }
    else
      return -65;

  }

  ///////////////////////////////////////////////////////

  public boolean deleteElement(String path){
    Key key = this.imgKeyGen(path);
    int j = searchElement(path);

    if(j != -65){

      if(j<0)
        j *= -1;

      if(this.hashTable[j].pxID.equals(key.pxID)){
        if(this.hashTable[j].next != null){
          this.hashTable[j] = this.hashTable[j].next;
        }
        else{
          this.hashTable[j] = null;
        }
      }
      else{
        Key primero = this.hashTable[j];
        Key segundo = this.hashTable[j].next;
        boolean borrado = false;

        while(!borrado){
          if(segundo.pxID.equals(key.pxID) && segundo.next == null){
            primero.next = null;
            borrado = true;
          }
          else if(segundo.pxID.equals(key.pxID) && segundo.next != null){
            primero.next = segundo.next;
            borrado = true;
          }
          else{
            primero = primero.next;
            segundo = segundo.next;
          }
        }
      }
      return true;
    }

    return false;

  }

  ///////////////////////////////////////////////////////

  public String[] printTable(){
    String[] returnString = new String[this.m];

    for (int i=0; i<this.m; i++) {

      System.out.println("*** INDICE " + i + " ***");

      if(this.hashTable[i] != null){
        Key start = this.hashTable[i];
        returnString[i] = start.path;
        start = start.next;

        while(start != null){
          returnString[i] += ", " + start.path;
          start = start.next;
        }

        System.out.println(returnString[i] + "\n");
      }
    }

    return returnString;
  }

  ///////////////////////////////////////////////////////

  public Key imgKeyGen(String path) {

    BufferedImage img = null;
    int pxAvobe = 0;
    String pxID = "";

    try {
      File file = new File(path);
      img = ImageIO.read(file);

      Image thumbnail = img.getScaledInstance(8, 8, Image.SCALE_SMOOTH);
      img = new BufferedImage(thumbnail.getWidth(null), thumbnail.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);

      img.getGraphics().drawImage(thumbnail, 0, 0, null);

      int[] pixels = img.getRGB(0, 0, 8, 8, null, 0, 8);
      int colourAverage = 0;

      for (int i=0; i<pixels.length; i++) {
        colourAverage += pixels[i];
        pxID += (pixels[i]*-1);
      }

      colourAverage = colourAverage / 64;

      for (int i=0; i<pixels.length; i++) {
        if(pixels[i] >= colourAverage)
          pxAvobe++;
      }

    }

    catch (IOException e) {  System.out.println("Error al leer la imagen"); }

    return new Key(pxAvobe, path, pxID, null);

  }

  ///////////////////////////////////////////////////////

  public String getPath(int j){
      String path = this.hashTable[j].path;

      if(this.nodeNum != 0){
        Key tmp = this.hashTable[j];
        for(int i=0; i<this.nodeNum; i++){
          tmp = tmp.next;
        }
        path = tmp.path;
      }

      return path;
  }

  //////////////////////////////////////////////////////

  // public int getNodeNum(){
  //   return this.nodeNum;
  // }

  // public static void main(String[] args) {
  //   HashT ht = new HashT();
  //   ht.insertElement("img/img1-1.jpeg");
  //   ht.insertElement("img/img1-1.jpeg");
  //   ht.insertElement("img/img1-2.jpeg");
  //   //ht.deleteElement(ht.imgKeyGen("img/img1-1.jpeg"));
  //   //System.out.println(ht.searchElement(ht.imgKeyGen("img/img1-1.jpeg")));
  //   ht.printTable();
  // }

}
