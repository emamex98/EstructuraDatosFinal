/*
  ______ __  __          __  __ ________   _____   ___
 |  ____|  \/  |   /\   |  \/  |  ____\ \ / / _ \ / _ \
 | |__  | \  / |  /  \  | \  / | |__   \ V / (_) | (_) |
 |  __| | |\/| | / /\ \ | |\/| |  __|   > < \__, |> _ <
 | |____| |  | |/ ____ \| |  | | |____ / . \  / /| (_) |
 |______|_|  |_/_/    \_\_|  |_|______/_/ \_\/_/  \___/

Emanuel Estrada Larios - A01633605
*/

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

class LinearProbeHash {

  private int m, i, numberOfKeys;
  private Key[] hashTable;
  private Key deleted;

  public LinearProbeHash(int tableSize){
    this.m = tableSize;
    this.hashTable = new Key[this.m];
    this.deleted = new Key(0,"");
    insertElement(deleted);
  }

  /////////////////////////////////////////////////////////

  private int hash(Key key){
    return (auxHash(key) + this.i) % this.m;
  }

  /////////////////////////////////////////////////////////

  private int auxHash(Key key){
    return key.k;
  }

  //////////////////////////////////////////////////////////

  public int insertElement(Key key){
    this.i = 0;
    int j = 0;

    while(this.i < this.m){
      j = hash(key);
      if(hashTable[j] == null || hashTable[j] == this.deleted){
        hashTable[j] = key;
        System.out.println("Elemento guardado en indice: " + j);
        return j;
      }
      else{
        this.i++;
      }
    }

    return 0;
  }

  ////////////////////////////////////////////////////////////

  public int[] searchElement(Key key){
    this.i = 0;
    int j = 0;

    while(this.i < this.m){
      j = hash(key);
      if(hashTable[j] != null && hashTable[j] != this.deleted){
        if (hashTable[j].k == key.k){
          int[] fullMatch = {j};
          return fullMatch;
        }
        else
          this.i++;
        }
      else
        break;
    }
      j = hash(key);
      int[] similarImg = new int[5];

      if(hashTable[j] != null && hashTable[j] != this.deleted)
        similarImg[0] = j;
      if(hashTable[(j-1) % this.m] != null)
        similarImg[1] = (j-1) % this.m;
      if(hashTable[(j+1) % this.m] != null)
        similarImg[2] = (j+1) % this.m;
      if(hashTable[(j-2) % this.m] != null)
        similarImg[3] = (j-2) % this.m;
      if(hashTable[(j+2) % this.m] != null)
        similarImg[4] = (j+2) % this.m;

      return similarImg;
  }

  ////////////////////////////////////////////////////////////

  public void deleteElement(Key key){
    int[] j = searchElement(key);
    hashTable[j[0]] = this.deleted;
  }

  ///////////////////////////////////////////////////////////

  public Key getIndex(int j){
    return hashTable[j];
  }

  ////////////////////////////////////////////////////////////

  public void printTable(){

    try {
      FileWriter fw = new FileWriter("registros.txt");
      PrintWriter pw = new PrintWriter(fw);
      for(int i=0; i<this.m; i++){
        if(this.hashTable[i] != null)
          pw.println(i + ": " + this.hashTable[i].data);
      }
      pw.close();
      System.out.println("Se creó el archivo correctamente.");
    }
    catch (IOException e) {
      System.out.println("Error al intentar crear el archivo.");
    }

  }

}
