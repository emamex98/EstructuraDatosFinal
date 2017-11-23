/*
--------------------------------------
Estructura de Datos, LuJu 8:30
Proyeto Final
--------------------------------------
Emanuel Estrada Larios - A01633605
Sebastian Cedeno Gonzalez
--------------------------------------
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.*;
import java.net.*;
import javax.swing.filechooser.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class ImageFinder extends JPanel implements ActionListener{
  private static final int BUFFER_SIZE = 4096;

  private HashT finder;
  private File inputImg, matchImg, foundImg;

  private JTextField tfImagen;
  private JButton btExaminar, btAdd, btSearch, btDlt, btVarias, btZip;

  private ImagePanel display;

  public ImageFinder(ImagePanel display){
    super();
    this.setPreferredSize(new Dimension(500,300));

    this.finder = new HashT();
    this.display = display;

    this.add(new JLabel("<html> <center> <br> <b> Proyecto Final <br> Emanuel Estrada Larios <br> Sebastian Cedeno Gonzalez <br> </center></html>"));

    this.tfImagen = new JTextField(25);
    this.add(this.tfImagen);

    this.btExaminar = new JButton("Examinar...");
    this.btExaminar.addActionListener(this);
    this.add(this.btExaminar);

    this.btAdd = new JButton("Agregar");
    this.btAdd.addActionListener(this);
    this.add(this.btAdd);

    this.btSearch = new JButton("Buscar");
    this.btSearch.addActionListener(this);
    this.add(this.btSearch);

    this.btDlt = new JButton("Eliminar");
    this.btDlt.addActionListener(this);
    this.add(this.btDlt);

    this.btVarias = new JButton("Importar Carpeta");
    this.btVarias.addActionListener(this);
    this.add(this.btVarias);

    this.btZip = new JButton("Zip");
    this.btZip.addActionListener(this);
    this.add(this.btZip);
  }

  //////////////////////////////////////////////////////////

  public void actionPerformed(ActionEvent e){

    if(e.getSource() == this.btExaminar){

      JFileChooser chooser = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter( "Imagenes", "jpg", "gif", "jpeg", "png");
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this);

      if(returnVal == JFileChooser.APPROVE_OPTION) {
        this.inputImg = chooser.getSelectedFile();
        this.tfImagen.setText(this.inputImg.getName());
        this.revalidate();
      }
    }

    else if(e.getSource() == this.btAdd){
      this.addImage(this.inputImg, false);
      this.display.updatePanel(this.inputImg);
      finder.printTable();
    }

    else if(e.getSource() == this.btSearch){
      int result = this.searchImage(this.inputImg);
      this.foundImg = new File(finder.getPath(result));
      this.display.updateFound(this.foundImg);
    }

    else if(e.getSource() == this.btDlt){
      this.deleteImage(this.inputImg);
      this.display.updatePanel(null);
      finder.printTable();
    }
    else if(e.getSource()== this.btVarias){
        JFileChooser chooser=new JFileChooser();
    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	int rV = chooser.showOpenDialog(this);
    	if(rV== JFileChooser.APPROVE_OPTION){
    		String rA = chooser.getSelectedFile().toString();
    		this.contentFolder(rA);
    	}
      finder.printTable();
    }
    else if(e.getSource()==this.btZip){
        JFileChooser chooser=new JFileChooser();
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("zip");
        //leFilter(filter);
        int returnVal = chooser.showOpenDialog(this);

        if(returnVal==JFileChooser.APPROVE_OPTION){
            String zip = chooser.getSelectedFile().toString();
            String zipDes = zip.substring(0,zip.length()-4);
            System.out.println(chooser.getName());

            try {
				      this.unzip(zip, zipDes);
              JOptionPane.showMessageDialog(null,"Se descomprimio la carpeta exitosamente");
			      }
            catch (IOException e1) {
				       e1.printStackTrace();
			      }

        }

    }

  }

  ///////////////////////////////////////////////////////////

  public void contentFolder(String f){
	  File directory=new File(f);
	  File[] cOF=directory.listFiles();
	  for(File object: cOF){
		  addImage(object,true);
	  }
    JOptionPane.showMessageDialog(null,"Se importaron las imagenes de la carpeta");
  }

  ///////////////////////////////////////////////////////////

  private int imgKeyGen (File img) {

    BufferedImage originalImg = null;
    int pxAvobe = 0, pxBelow = 0;

    try {
      originalImg = ImageIO.read(img);

      Image thumbnail = originalImg.getScaledInstance(8, 8, Image.SCALE_SMOOTH);

      BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null), thumbnail.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);

      bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);

      int[] pixels = bufferedThumbnail.getRGB(0, 0, 8, 8, null, 0, 8);
      int colourAverage = 0;

      for (int i=0; i<pixels.length; i++) {
        colourAverage += pixels[i];
      }

      colourAverage = colourAverage / 64;

      for (int i=0; i<pixels.length; i++) {
        if(pixels[i] <= colourAverage)
          pxBelow++;
        else
          pxAvobe++;
      }

    }
    catch (IOException e) {  System.out.println("Error"); }
    return Integer.parseInt("" + pxBelow + pxAvobe);
  }

  //////////////////////////////////////////////////////////

  public void addImage(File img, boolean varias){
    int j = finder.insertElement(img.getParent() + "/" + img.getName());
    System.out.println("Imagen agregada en indice " + j);

    if(!varias)
      JOptionPane.showMessageDialog(null,"La imagen fue agregada exitosamente - Indice: #" + j);
  }

  //////////////////////////////////////////////////////////

  public int searchImage(File img){
    int j = finder.searchElement(img.getParent() + "/" + img.getName());

    if(j >= 0){
      JOptionPane.showMessageDialog(null,"Imagen encontrada en indice " + j);
      System.out.println("Imagen encontrada en indice " + j);
    }
    else if(j == -65){
      JOptionPane.showMessageDialog(null,"Imagen no encontrada");
    }
    else {
      JOptionPane.showMessageDialog(null,"Imagen no encontrada, similares en indice " + j);
    }

    return j;
  }

  ////////////////////////////////////////////////////////////

  public void deleteImage(File img){
    boolean deleted = finder.deleteElement(img.getParent() + "/" + img.getName());

    if(deleted)
      JOptionPane.showMessageDialog(null,"Imagen eliminada");
    else
      JOptionPane.showMessageDialog(null,"Imagen no encontrada");

    // if(k.length != 1){
    //   finder.deleteElement(imgK);
    //   JOptionPane.showMessageDialog(null,"Imagen eliminada.");
    // }
    // else {
    //   JOptionPane.showMessageDialog(null,"No se encontro la imagen con dicha llave.");
    // }

  }

  // /////////////////////////////////////////////////////////

    public void unzip(String zipFilePath, String destDirectory) throws IOException {

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    ///////////////////

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

  // public static void main(String[] args) {
  //   ImageFinder ih = new ImageFinder(null);
  //
  //   ih.addImage(new File("img/img1-1.jpeg"));
  //   // //ih.addImage(new File("img/img1-2.jpeg"));
  //   //
  //   // ih.searchImage(new File("img/img1-1.jpeg"));
  //   // ih.searchImage(new File("img/img1-2.jpeg"));
  //   //
  //   //
  //   // ih.addImage(new File("img/img1-2.jpeg"));
  //   // ih.deleteImage(new File("img/img1-2.jpeg"));
  //   // ih.searchImage(new File("img/img1-2.jpeg"));
  //   //
  // }

}
