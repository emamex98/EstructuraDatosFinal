/*
--------------------------------------
Estructura de Datos, LuJu 8:30
Proyeto Final
--------------------------------------
Emanuel Estrada Larios - A01633605
Sebastian Cedeno Gonzalez
--------------------------------------
*/

class Key{

  public int k;
  public String path, pxID;
  public Key next;

  public Key(int k, String path, String pxID, Key next){
    this.k = k;
    this.path = path;
    this.pxID = pxID;
    this.next = next;
  }

}
