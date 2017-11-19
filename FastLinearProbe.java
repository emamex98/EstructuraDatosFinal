/*
--------------------------------------
Estructura de Datos, LuJu 8:30
Proyeto Final
--------------------------------------
Emanuel Estrada Larios - A01633605
Sebastian Cedeno Gonzalez
--------------------------------------
*/

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;

public class FastLinearProbe {

  private int m, i, numberOfKeys;
  private double maxFactor, rbFactor;

  private Key[] hashTable;
  //private Key deleted;

  /////////////////////////////////////////////////////////

  /* Constructor de FastLinearProbe:
      - Incializa table con tamano de numero primo
      - Agrega elemento dummy en indice 0
  */

  public FastLinearProbe(){
    this.m = 100000;
    this.hashTable = new Key[this.m];
    this.maxFactor = 0.9;
    this.rbFactor = Math.floor(Math.log(this.m) / Math.log(2));
  }

  /////////////////////////////////////////////////////////

  /* Funcion hash (open adressing):
      - Regresa el indice calculado para la llave correspondiente
   */

  private int hash(Key key){
    return (key.k + this.i) % this.m;
  }

  //////////////////////////////////////////////////////////

  /* Funcion insertElement:
     - Regresa el indice del table donde se guardo el elemento
  */

  public int insertElement(Key key){

    if(this.numberOfKeys > (this.m * this.maxFactor)){
      this.resizeTable(1.5);
    }

    /* Implementacion de Robin Hood Hashing:
        - rbFactor: Limite maximo de probes permitidos
    */

    this.i = 0;
    int j = 0;
    int idealHash = hash(key);

    while(this.i < this.m){
      j = hash(key);

      if(hashTable[j] == null){
        hashTable[j] = key;
        //System.out.println("Elemento guardado en indice: " + j);
        key.dib = this.i;
        this.numberOfKeys++;
        break;
      }
      else {
        this.i++;
      }
    }

    // NO FUNCIONA
    // if(this.i !=0){
    //   Key tmp = this.hashTable[j];
    //   while(this.hashTable[idealHash] != null){
    //     this.hashTable[j] = this.hashTable[j-1];
    //     j--;
    //   }
    //   this.hashTable[idealHash] = tmp;
    // }

    // Funciona MASO
    if(this.i != 0){
      boolean rhSwap = true;
      int h = (j-1) % this.m;
      while(rhSwap && h>0){
        if(hashTable[j].dib < hashTable[h].dib){
          Key tmp = hashTable[h];
          hashTable[h] = hashTable[j];
          hashTable[j] = tmp;
          j = h;
          h = (j-1) % this.m;
        }
        else{
          rhSwap = false;
        }
      }
    }

    return j;
  }

  ////////////////////////////////////////////////////////////

  /* Resizer del hash table:
      - Redeclara el tamano de la table a m * 3/2
      - Rehashea todos los elementos existentes
  */

  public void resizeTable(double rFactor){
    this.rbFactor = Math.floor(Math.log(this.m) / Math.log(2));

    ////
    Key[] tmp = this.hashTable.clone();
    System.out.println(" *** \n " + this.m);
    int usedSlots = 0;
    for(int j=0; j<tmp.length; j++){
      if(tmp[j] != null)
        usedSlots++;
    }

    //System.out.println(" rbFactor: " + this.rbFactor);
    //System.out.println(" Slots utilizados: " + usedSlots  + " \n ***");
    ////


    //Key[] tmp = this.hashTable.clone();
    this.m *= rFactor;
    boolean tamanoPrimo = this.esPrimo(this.m);

    while(tamanoPrimo == false){
      this.m++;
      tamanoPrimo = this.esPrimo(this.m);
    }

    this.hashTable = new Key[this.m];

    for(int j=0; j<tmp.length; j++){
      if(tmp[j] != null)
        insertElement(tmp[j]);
    }

  }

  ////////////////////////////////////////////////////////////

  private boolean esPrimo(int n) {
    if (n%2 == 0)
      return false;
    for(int i=3;i*i<=n;i+=2){
      if(n%i==0)
        return false;
    }
    return true;
  }

  ////////////////////////////////////////////////////////////

  public void printAll(){
    int eE = 0;
    for(int j=0; j<this.hashTable.length; j++){
      if(hashTable[j] != null){
        System.out.println("Index: " + j + " - Llave: " + hashTable[j].k);
        eE++;
      }
    }
    System.out.println("Elementos: " + eE);
  }

  ////////////////////////////////////////////////////////////


  public static void main(String[] args) {

    FastLinearProbe lh = new FastLinearProbe();
    Random random = new Random();

    int key;
    String preKey;

    for(int i=0; i<10000; i++){
      key = random.nextInt(64);
      preKey = Integer.toString(key);
      key = 64-key;
      preKey +=  Integer.toString(key);
      key = Integer.parseInt(preKey);
      lh.insertElement(new Key(key,"img/" + i));
    }

    // lh.insertElement(new Key(2737,"Elemento 2"));
    // lh.insertElement(new Key(2737,"Elemento 3"));
    // lh.insertElement(new Key(2737,"Elemento 4"));
    // lh.insertElement(new Key(2737,"Elemento 5"));
    // lh.insertElement(new Key(2737,"Elemento 6"));
    // lh.insertElement(new Key(2737,"Elemento 7"));
    // lh.insertElement(new Key(2737,"Elemento 8"));
    // lh.insertElement(new Key(2737,"Elemento 9"));
    // lh.insertElement(new Key(2737,"Elemento 10"));
    // lh.insertElement(new Key(2737,"Elemento 11"));
    // lh.insertElement(new Key(2737,"Elemento 12"));
    // lh.insertElement(new Key(2737,"Elemento 13"));
    // lh.insertElement(new Key(2737,"Elemento 14"));

    // System.out.println("\nTabla original:");
    // lh.printAll();
    // // lh.resizeTable();
    //
    // lh.insertElement(new Key(2737,"Elemento 15"));

    System.out.println("\nTabla nueva:");
    lh.printAll();
  }

}
