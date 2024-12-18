/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entornoVisual;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import logica.Configuraciones;
import logica.Episodio;
import logica.Estado;
import logica.Politica;
import logica.PoliticaEGreedy;
import logica.PoliticaSoftMax;
import logica.QMat;
import logica.RMat;
/**
 *
 * @author Maty
 */
public class MainWindow extends javax.swing.JFrame{
    
    public static Boolean banderaEstadoFinal;
    public static Boolean estadoInicial;
    private Border blackline;
    private Boolean flagFinal;
    private Boolean flagInicial;
    private static Politica p;
    private int contadorEpisodios;
    public static boolean banderaTope=false;
    
    //Atributos para memorizar que habia antes del estado inicial
    public static JButton jbAnterior = new JButton();
    public static int ii;
    public static int jj;
    public static int abss;
    // ---------------------------------
   
    public static boolean banderaEGreedy;
    public static boolean banderaSoftMax;
    
    private Episodio[] episodios;
    
    
    private static boolean detener =false;
   
    private static int EpisodioStop=0;
    private static int TotalEpisodios=0;
    
    public static int cont;
    public static int topeEpisodios;
    public static QMat matrizQ; 
    public static Estado estadoFinal;
    public static RMat mat;
    
    public static boolean reinicio=false;
          
    public static int avanzoUnaVez=0;
    
    private ColoresyFormas cf = new ColoresyFormas();
    
    EvJBGrande ma               = new EvJBGrande();
    EvJBChico  ma1              = new EvJBChico();
    EvJBEstadoInicial ma2       = new EvJBEstadoInicial();

    
    
    MouseAdapter mWheelMovedCambiarEstados = new MouseAdapter(){
        public void mouseWheelMoved(MouseWheelEvent e){                   
            if(!Configuraciones.getFlagEvEstadoInicial()){
                if(Configuraciones.getDimension() < 8){
                    ma.setjbEstado(e);
                    ma.getjbEstado().repaint();
                }
                else{
                    ma1.setjbEstado(e);
                    ma1.getjbEstado().repaint();
                }
             }     
        }
    };
        
    MouseAdapter mClickedCambiarEstados = new MouseAdapter(){
        public void mouseClicked(MouseEvent e){
            if(!Configuraciones.getFlagEvEstadoInicial()){
                if(Configuraciones.getDimension() < 8){
                    ma.setjbEstado(e);
                    ma.getjbEstado().repaint();
                }
                else{
                    ma1.setjbEstado(e);
                    ma1.getjbEstado().repaint();
                }
            }
        }
    };   
    
    MouseAdapter mClickedEstadoInicial = new MouseAdapter(){
        public void mouseClicked(MouseEvent e){
            if(!Configuraciones.getFlagEvEstadoInicial()){
                if(Configuraciones.getDimension() < 8){
                    ma2.setjbEstado(e);
                    ma2.getjbEstado().repaint();
                }
                else{
                    ma2.setjbEstado(e);
                    ma2.getjbEstado().repaint();
                }
            }
        }
    };     
    
    
    public void setFlagInicial(Boolean flagInicial) {
        this.flagInicial = flagInicial;
    }
    public boolean getFlagInicial(){
        return this.flagInicial;
    }
    public void setFlagFinal(boolean flag){
        this.flagFinal = flag;
    }
    public boolean getFlagFinal(){
        return this.flagFinal;
    }
    
    
    public void iniciarEntrenamiento(){
         
        try{   
            final SwingWorker iniciarEntrenamiento;
            iniciarEntrenamiento = new SwingWorker(){
        @Override
        protected Object doInBackground() throws Exception {
        //imprimo en el label el contador de episodios    
        jLabelContador.setText(String.valueOf(contadorEpisodios));

        mat= obtenerRdesdePantalla();//obtiene la matriz Q del arreglo de botones

        mat.setInicial(Configuraciones.filaI, Configuraciones.colI);//Almaceno las posiciones del estado Inicial si existiese
        mat.setFinal(Configuraciones.filaF, Configuraciones.colF);//Almaceno las posiciones del estado Final
        mat.imprimirTab(); //imprimo por consola la matriz R

        matrizQ= new QMat(mat);//creo una nueva matriz Q
        System.out.println(matrizQ);//imprimo por consola la matriz Q

        //obtengo el estado final
        estadoFinal= matrizQ.getEstado(Configuraciones.getFilaF(),Configuraciones.getColF());
        //imprimo el estado final
        System.out.println("Estado final");
        System.out.println(estadoFinal);
        //Creo un arreglo de episodios
        episodios=new Episodio[Configuraciones.cantEpisodios]; 
        //seteo valores para controlar el entrenamiento
        cont=0;
        
        TotalEpisodios=Configuraciones.cantEpisodios;
        contadorEpisodios=0;
          
        if(Integer.parseInt(jTextCantidadEpisodios.getText().trim())>2000){//controla el tope de episodios
            topeEpisodios=Integer.parseInt(jTextCantidadEpisodios.getText().trim());
        } 
        else{
            topeEpisodios=2000;
        }    
          avanzar(cont, topeEpisodios, matrizQ, estadoFinal, mat);//Avanza en el entrenamiento

          System.out.println(episodios[Configuraciones.cantEpisodios-1].getMatrizQ());//imprime el ultimo episodio 

          throw new UnsupportedOperationException("Not supported yet.");

        }

        };
            
        iniciarEntrenamiento.execute();

        }catch (Exception e){
            JOptionPane.showMessageDialog(this,"Error" , e.toString(), 1);
        }
    }
    
    
     public void reanudarEntrenamiento(){
         
        try{   
            final SwingWorker reanudarEntrenamiento;
            reanudarEntrenamiento = new SwingWorker(){
            @Override
            protected Object doInBackground() throws Exception {

                  if(detener==true){//controlo si se prendio el boton de detener
                     detener=false;  
                     contadorEpisodios=EpisodioStop;
                  }  

                  avanzar(cont, topeEpisodios, matrizQ, estadoFinal, mat);

                  System.out.println(episodios[Configuraciones.cantEpisodios-1].getMatrizQ()); 

                  throw new UnsupportedOperationException("Not supported yet."); 
            }
        };
            
        reanudarEntrenamiento.execute();

        }catch (Exception e){
            JOptionPane.showMessageDialog(this,"Error" , e.toString(), 1);
        }
    }
    
    
     private void avanzar(int cont, int TopeEpisodios, QMat matrizQ, Estado estadoFinal, RMat mat) throws NumberFormatException {
        
        //mientras no se llegue al tope de episodios y no este detenido voy creando episodios nuevos y se almacenan en el arreglo de episodios
        while((contadorEpisodios<TotalEpisodios)&&(cont<TopeEpisodios)&&(!detener)){
            episodios[contadorEpisodios]= new Episodio(matrizQ,estadoFinal,p,mat,contadorEpisodios);
            contadorEpisodios++;
            jLabelContador.setText(String.valueOf(contadorEpisodios));
            cont++;
        }
                       
        String userdata = jTextCantidadEpisodios.getText().trim();//controlo que si llega al valor tope de Episodios. Habilito el boton Avanzar
        int val = Integer.parseInt(userdata);
        if(contadorEpisodios==val){
            
            jBAvanza1.setEnabled(false);
            jbDetener.setEnabled(false);
            jbReanudar.setEnabled(false);
            cont=0;
            
            this.agregarEvEstadoInicial();
        }
    }
     
    public void reanudarEntrenamientoUnaVez(){
         
        try{   
            final SwingWorker reanudarEntrenamientoUnaVez;
            reanudarEntrenamientoUnaVez = new SwingWorker(){
            @Override
            protected Object doInBackground() throws Exception {

                  if(detener==true){//controlo si se prendio el boton de detener
                     detener=false;  
                     contadorEpisodios=EpisodioStop;
                  }  
                  //avanza de a un movimiento
                  avanzarUnaVez(cont, topeEpisodios, matrizQ, estadoFinal, mat);

                  System.out.println(episodios[Configuraciones.cantEpisodios-1].getMatrizQ()); 

                  throw new UnsupportedOperationException("Not supported yet."); 
            }
        };
            
        reanudarEntrenamientoUnaVez.execute();

        }catch (Exception e){
            JOptionPane.showMessageDialog(this,"Error" , e.toString(), 1);
        }
    }
    
    private void avanzarUnaVez(int cont, int TopeEpisodios, QMat matrizQ, Estado estadoFinal, RMat mat) throws NumberFormatException {
        //Si no se llegua al tope de episodios y no este detenido creo un episodio nuevo y lo almaceno en el arreglo de episodios           
        if((contadorEpisodios<TotalEpisodios)&&(cont<TopeEpisodios)&&(!detener)){
            episodios[contadorEpisodios]= new Episodio(matrizQ,estadoFinal,p,mat,contadorEpisodios);
            contadorEpisodios++;
            jLabelContador.setText(String.valueOf(contadorEpisodios));
            cont++;
        }
                       
        String userdata = jTextCantidadEpisodios.getText().trim();//controlo que si llega al valor tope de Episodios. Habilito el boton Avanzar
        int val = Integer.parseInt(userdata);
        if(contadorEpisodios==val){
            
            jBAvanza1.setEnabled(false);
            jbDetener.setEnabled(false);
            jbReanudar.setEnabled(false);
            //this.agregarEvEstadoInicial();
        }
    }
    public MainWindow() {
        
        initComponents();
        MainWindow.vistaConfigPoliticas(true);
        MainWindow.jlAusenciaEstadoFinal.setVisible(false);
        jPanel1.setVisible(false);
        
        jbEmpezarDeNuevo.setVisible(false);
        jCGamma.setVisible(true);
        jCEpsilon.setVisible(true);
        jCEpsilon.setEnabled(true);
        jCGamma.setEnabled(true);
        jCTau.setVisible(false);
        jlEstadoInicial.setVisible(false);
        jbGenerarTablero.setEnabled(true);

        jcbDim.setFocusable(true);
    }
    
   
    public void cargarTableroManual(int dim){
        
        Configuraciones.setDimension(dim);
        jpTablero.setVisible(false);
        jpTablero.removeAll();
        jpTablero.setLayout(new GridLayout(dim,dim));
          
        blackline = BorderFactory.createLineBorder(Color.black);
         
        for (int i = 0; i < dim; i++) {
            String posI=Integer.toString(i);
            for (int j = 0; j < dim; j++) {
                String posJ=Integer.toString(j);
                JButton jbEstado = new JButton();
                jbEstado.setBorder(blackline);
                jbEstado.setFont(cf.font);
                jbEstado.setBackground(Color.white);
                
                jbEstado.addMouseWheelListener(mWheelMovedCambiarEstados);                  
                jbEstado.addMouseListener(mClickedCambiarEstados);                  
                jbEstado.setText("( "+ posI+", "+ posJ + " )");              
                jbEstado.setName("( "+ posI+", "+ posJ + " )");
                jpTablero.add(jbEstado);           
            }
        }
            //  No activa los botones confirmar politica y guardar escenario 
            // porque no tiene estado Final la generacion manual 
            // al principio
            estadoInicial = false;
            banderaEstadoFinal = false;
            jbConfirmar.setVisible(true);
            jbConfirmar.setEnabled(false);
            
            jlAusenciaEstadoFinal.setVisible(true);
            jlEstadoInicial.setVisible(false);
 
        jpTablero.setVisible(true);       
        }

    public void cargarTableroAleatorio(int dim){
        //instanciar matriz, aleatoriament, y modificar codigo para que tome valores de esa matriz
       
        Configuraciones.setDimension(dim);
        jpTablero.setVisible(false);
        jpTablero.removeAll();
        jpTablero.setLayout(new GridLayout(dim,dim));
        
        blackline = BorderFactory.createLineBorder(Color.black);
        
        MainWindow.vistaConfigPoliticas(true);
                
        for (int i = 0; i < dim; i++) {
            String posI=Integer.toString(i);
            for (int j = 0; j < dim; j++) {
                String posJ=Integer.toString(j);
                JButton jbEstado = new JButton();
                jbEstado.setBorder(blackline);
                
                jbEstado.setFont(cf.font);
                jbEstado.setText("( "+ posI+", "+ posJ + " )");
                jbEstado.setName("( "+ posI+", "+ posJ + " )");
                if (dim < 8){
                    switch(this.aleatorio(1, 5)){
                    
                        case 1: jbEstado.setBackground(Color.white);
                            break;
                        case 2: jbEstado.setBackground(Color.black);
                            break;
                        case 3:jbEstado.setBackground(cf.rojo);
                            
                            break;
                        case 4: jbEstado.setBackground(cf.amarillo);
                            
                            break;
                        case 5: jbEstado.setBackground(cf.verde);
                            
                            break;
                        default: //jbEstado.setBackground(Color.red);
                            jbEstado.setText("OTRO");
                            break;
                    }   
                }else{
                    switch(this.aleatorio(1, 5)){
                    
                        case 1: jbEstado.setBackground(Color.white);
                            break;
                        case 2: jbEstado.setBackground(Color.black);
                            break;
                        case 3: jbEstado.setBackground(cf.rojo);
                            
                            break;
                        case 4: jbEstado.setBackground(cf.amarillo);
                            
                            break;
                        case 5: jbEstado.setBackground(cf.verde);
                            
                            break;
                        default: //jbEstado.setBackground(Color.red);
                            jbEstado.setText("OTRO");
                            break;
                    }        
            }
            jbEstado.addMouseWheelListener(mWheelMovedCambiarEstados);                  
            jbEstado.addMouseListener(mClickedCambiarEstados);    
            
            //  Se activa los botones confirmar politica y guardar escenario 
            // porque  tiene estado Inicial y Final la generacion aleatoria
            // al principio
            estadoInicial = true;
            banderaEstadoFinal = true;
            jbConfirmar.setVisible(true);
            jbConfirmar.setEnabled(true);
            
            jlAusenciaEstadoFinal.setVisible(false);
            jlEstadoInicial.setVisible(false);
            
            
            jpTablero.add(jbEstado);
            
        }
        }    
//        int aleatorio1 = this.posAbosAleatoria(dim);
        
        // Se seteaba el estado inicial (No hay que hacerlo es este momento)
//        jpTablero.remove(this.posAbosAleatoria(dim));
//        jpTablero.add(this.estadoInicial(dim), this.posAbosAleatoria(dim));
//        VentanaPrincipal.estadoInicial = true;
//        VentanaPrincipal.jlEstadoInicial.setVisible(false);
        
        int aleatorio2 = this.posAbosAleatoria(dim);
//        while(aleatorio1 == aleatorio2){
//            aleatorio2 = this.posAbosAleatoria(dim);
//        }
//        Se agrega Estado Final
        jpTablero.remove(aleatorio2);
        jpTablero.add(this.estadoFinal(dim), aleatorio2);
        MainWindow.banderaEstadoFinal = true;
        MainWindow.jlAusenciaEstadoFinal.setVisible(false);

        
        jpTablero.setVisible(true);
  }
    
    
    public int aleatorio(int min, int max){
        
        int randomNum=0;
        Random rn = new Random();
       // int n = max - min + 1;
        int n = rn.nextInt(max);
        //int i = rn.nextInt() % n;
        //randomNum =  min + i;
        //return randomNum;
        return n+1;
    }
    
    public int aleatorioConCero(int max){
        Random rn = new Random();
        int n = rn.nextInt(max);
        return n;
    }
    
//    Devuelve una posición absoluta aleatoria para usar en el GridLayout
//            de la Interfaz grafica. 
//    Verificar que abarque todos los estados
    public int posAbosAleatoria(int dim){   
        
        int fila = this.aleatorioConCero(dim);
        int columna = this.aleatorioConCero(dim);
        
        return ((fila*dim)+columna);
    }
    
    public JButton estadoFinal(int dim){
        blackline = BorderFactory.createLineBorder(Color.black);
 
        JButton jbEstado = new JButton();
        jbEstado.setBorder(blackline);
        jbEstado.setFont(cf.font);
        jbEstado.setBackground(Color.GREEN);
        
        if(dim < 8){
            jbEstado.setText("FINAL");
            jbEstado.setBackground(Color.GREEN);    
        }
        else{
            jbEstado.setText("F");
            jbEstado.setBackground(Color.GREEN);     
        }
        jbEstado.addMouseWheelListener(mWheelMovedCambiarEstados);                  
        jbEstado.addMouseListener(mClickedCambiarEstados);      
        
        MainWindow.jbConfirmar.setEnabled(true);
        jbEstado.repaint();
        return jbEstado; 
        
    }
    
    public JButton estadoInicial(int dim){
        blackline = BorderFactory.createLineBorder(Color.black);
 
        JButton jbEstado = new JButton();
        jbEstado.setBorder(blackline);
        jbEstado.setFont(cf.font);
        jbEstado.setBackground(Color.ORANGE);

        if(dim < 8){
            jbEstado.setText("INICIAL");
            jbEstado.setBackground(Color.ORANGE);
            jbEstado.addMouseWheelListener(new MouseAdapter(){
                    public void mouseWheelMoved(MouseWheelEvent e){             
                           ma.setjbEstado(e);
                           ma.getjbEstado().repaint();
                        }
                    });
//                  
                    jbEstado.addMouseListener(new MouseAdapter(){
                        public void mouseClicked(MouseEvent e){                    
                            ma.setjbEstado(e);           
                            ma.getjbEstado().repaint();
                        }
                    }); 
        }
        else{
            jbEstado.setText("I");
            jbEstado.setBackground(Color.ORANGE);
            jbEstado.addMouseWheelListener(new MouseAdapter(){
                        public void mouseWheelMoved(MouseWheelEvent e){           
                           ma1.setjbEstado(e);
                           ma1.getjbEstado().repaint();
                        }
                    });
//                  
                    jbEstado.addMouseListener(new MouseAdapter(){
                        public void mouseClicked(MouseEvent e){          
                           ma1.setjbEstado(e);
                           ma1.getjbEstado().repaint();
                        }
                    });            
        }
    
        jbEstado.repaint();
        return jbEstado;     
    }
    
    public static void vistaConfigPoliticas(Boolean flag){
        
        //Primer cuadrante
        jlConfig.setVisible(flag);
        jlDim.setVisible(flag);
        jlManOAlea.setVisible(flag);
        
        jcbDim.setVisible(flag);
        jrbManual.setVisible(flag);
        jrbAuto.setVisible(flag);  
        
        //Segundo cuadrante
        jlGammaTitulo.setVisible(flag);
        jlEpsilon.setVisible(flag);
        jlTau.setVisible(false); //Por defecto
        jlGamma.setVisible(flag);
        
        jCEpsilon.setVisible(flag);
        jCTau.setVisible(false); // Falso por defecto
        jCGamma.setEnabled(flag);
        
        //Tercer Cuadrante
        jlRecompensas.setVisible(flag);
        jlMalo.setVisible(flag);
        jlRegular.setVisible(flag);
        jlbueno.setVisible(flag);
        jlExcelente.setVisible(flag);
        jlFinal.setVisible(flag);
        jlInicialQ.setVisible(flag);
        
        jtfMalo.setVisible(flag);
        jtfRegular.setVisible(flag);
        jtfBueno.setVisible(flag);
        jtfExcelente.setVisible(flag);
        jtfFinal.setVisible(flag);
        jtfInicialQ.setVisible(flag);
        
        //Quinto Cuadrante
        jbGenerarTablero.setVisible(flag);
        jbConfirmar.setVisible(false); //Por defecto
        
        
        // Otra cosa
        jtfBueno.setVisible(flag);
        jtfBueno.setText(Double.toString(Configuraciones.getValorBueno()));
        jtfRegular.setVisible(flag);
        jtfRegular.setText(Double.toString(Configuraciones.getValorNeutro()));
        jtfMalo.setVisible(flag);
        jtfMalo.setText(Double.toString(Configuraciones.getValorMalo()));
        jtfExcelente.setVisible(flag);
        jtfExcelente.setText(Double.toString(Configuraciones.getValorExcelente()));
        jtfFinal.setVisible(flag);
        jtfFinal.setText(Double.toString(Configuraciones.getValorFinal()));
        jtfInicialQ.setVisible(flag);
        jtfInicialQ.setText(Integer.toString(Configuraciones.getValorPorDefectoMatQ()));
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jRadioButton1 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jpSuperior = new javax.swing.JPanel();
        jlConfig = new javax.swing.JLabel();
        jlDim = new javax.swing.JLabel();
        jcbDim = new javax.swing.JComboBox();
        jlManOAlea = new javax.swing.JLabel();
        jrbAuto = new javax.swing.JRadioButton();
        jrbManual = new javax.swing.JRadioButton();
        jbGenerarTablero = new javax.swing.JButton();
        jlRecompensas = new javax.swing.JLabel();
        jrbSoftMax = new javax.swing.JRadioButton();
        jrbEGreedy = new javax.swing.JRadioButton();
        jlTau = new javax.swing.JLabel();
        jlEpsilon = new javax.swing.JLabel();
        jCGamma = new javax.swing.JComboBox();
        jCTau = new javax.swing.JComboBox();
        jlMalo = new javax.swing.JLabel();
        jlRegular = new javax.swing.JLabel();
        jlbueno = new javax.swing.JLabel();
        jlExcelente = new javax.swing.JLabel();
        jlFinal = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jlGammaTitulo = new javax.swing.JLabel();
        jbConfirmar = new javax.swing.JButton();
        jtfInicialQ = new javax.swing.JTextField();
        jtfMalo = new javax.swing.JTextField();
        jtfRegular = new javax.swing.JTextField();
        jtfBueno = new javax.swing.JTextField();
        jtfExcelente = new javax.swing.JTextField();
        jtfFinal = new javax.swing.JTextField();
        jlInicialQ = new javax.swing.JLabel();
        jlGamma = new javax.swing.JLabel();
        jCEpsilon = new javax.swing.JComboBox();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jlConfigPoliticas = new javax.swing.JLabel();
        jpTablero = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTextCantidadEpisodios = new javax.swing.JTextField();
        jBEntrena = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jBAvanza = new javax.swing.JButton();
        jLabelItera = new javax.swing.JLabel();
        jLabelContador = new javax.swing.JLabel();
        jbEmpezarDeNuevo = new javax.swing.JButton();
        jbDetener = new javax.swing.JButton();
        jbReanudar = new javax.swing.JButton();
        jMatrizQ = new javax.swing.JButton();
        jBAvanza1 = new javax.swing.JButton();
        jBGrafica = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jBBorrar = new javax.swing.JButton();
        jlEstadoInicial = new javax.swing.JLabel();
        jlAusenciaEstadoFinal = new javax.swing.JLabel();

        jRadioButton1.setText("jRadioButton1");

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ventana Principal");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpSuperior.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlConfig.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlConfig.setText("Creación del escenario:");
        jpSuperior.add(jlConfig, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 150, -1));

        jlDim.setText("Dimensión:");
        jpSuperior.add(jlDim, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jcbDim.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "6x6", "7x7", "8x8", "9x9", "10x10" }));
        jcbDim.setNextFocusableComponent(jrbManual);
        jcbDim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDimActionPerformed(evt);
            }
        });
        jpSuperior.add(jcbDim, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        jlManOAlea.setText("Creación:");
        jpSuperior.add(jlManOAlea, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

        jrbAuto.setText("Automática");
        jrbAuto.setNextFocusableComponent(jrbEGreedy);
        jrbAuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbAutoActionPerformed(evt);
            }
        });
        jpSuperior.add(jrbAuto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, -1, -1));

        jrbManual.setSelected(true);
        jrbManual.setText("Manual");
        jrbManual.setNextFocusableComponent(jrbAuto);
        jrbManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbManualActionPerformed(evt);
            }
        });
        jpSuperior.add(jrbManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, -1, -1));

        jbGenerarTablero.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbGenerarTablero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Skip-forward24.png"))); // NOI18N
        jbGenerarTablero.setText("Generar escenario");
        jbGenerarTablero.setEnabled(false);
        jbGenerarTablero.setNextFocusableComponent(jbGenerarTablero);
        jbGenerarTablero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGenerarTableroActionPerformed(evt);
            }
        });
        jpSuperior.add(jbGenerarTablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 10, 210, 54));

        jlRecompensas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlRecompensas.setText("Valores de Recompensa:");
        jpSuperior.add(jlRecompensas, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 200, 20));

        jrbSoftMax.setText("SoftMax");
        jrbSoftMax.setNextFocusableComponent(jrbEGreedy);
        jrbSoftMax.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrbSoftMaxMouseClicked(evt);
            }
        });
        jrbSoftMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbSoftMaxActionPerformed(evt);
            }
        });
        jpSuperior.add(jrbSoftMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, -1, -1));

        jrbEGreedy.setSelected(true);
        jrbEGreedy.setText("E-Greedy");
        jrbEGreedy.setNextFocusableComponent(jrbSoftMax);
        jrbEGreedy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrbEGreedyMouseClicked(evt);
            }
        });
        jrbEGreedy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbEGreedyActionPerformed(evt);
            }
        });
        jpSuperior.add(jrbEGreedy, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, -1, 20));

        jlTau.setText("Tau: ");
        jlTau.setName(""); // NOI18N
        jpSuperior.add(jlTau, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, -1, 40));

        jlEpsilon.setText("Epsilon:");
        jlEpsilon.setName(""); // NOI18N
        jpSuperior.add(jlEpsilon, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, -1, 20));

        jCGamma.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9" }));
        jCGamma.setEnabled(false);
        jCGamma.setNextFocusableComponent(jtfMalo);
        jCGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCGammaActionPerformed(evt);
            }
        });
        jpSuperior.add(jCGamma, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 50, 20));

        jCTau.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", " " }));
        jCTau.setEnabled(false);
        jCTau.setNextFocusableComponent(jCGamma);
        jpSuperior.add(jCTau, new org.netbeans.lib.awtextra.AbsoluteConstraints(333, 60, 50, -1));

        jlMalo.setBackground(new java.awt.Color(240, 90, 82));
        jlMalo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlMalo.setText("Malo");
        jlMalo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jlMalo.setOpaque(true);
        jpSuperior.add(jlMalo, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 60, 30));

        jlRegular.setBackground(new java.awt.Color(255, 255, 255));
        jlRegular.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlRegular.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jlRegular.setOpaque(true);
        jpSuperior.add(jlRegular, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 60, 30));

        jlbueno.setBackground(new java.awt.Color(220, 246, 53));
        jlbueno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlbueno.setText("Bueno");
        jlbueno.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jlbueno.setOpaque(true);
        jpSuperior.add(jlbueno, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, 60, 30));

        jlExcelente.setBackground(new java.awt.Color(72, 237, 255));
        jlExcelente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlExcelente.setText("Excelente");
        jlExcelente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jlExcelente.setOpaque(true);
        jpSuperior.add(jlExcelente, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 40, 60, 30));

        jlFinal.setBackground(java.awt.Color.green);
        jlFinal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlFinal.setText("FINAL");
        jlFinal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jlFinal.setOpaque(true);
        jpSuperior.add(jlFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 70, 60, 30));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpSuperior.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 10, 120));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpSuperior.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, 10, 120));

        jlGammaTitulo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlGammaTitulo.setText("Factor de Aprendizaje:");
        jpSuperior.add(jlGammaTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 200, 20));

        jbConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Accept32.png"))); // NOI18N
        jbConfirmar.setText("Confirmar Configuración");
        jbConfirmar.setEnabled(false);
        jbConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConfirmarActionPerformed(evt);
            }
        });
        jpSuperior.add(jbConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 80, 210, 50));

        jtfInicialQ.setNextFocusableComponent(jbGenerarTablero);
        jpSuperior.add(jtfInicialQ, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 100, 70, 30));

        jtfMalo.setText(" ");
        jtfMalo.setNextFocusableComponent(jtfRegular);
        jtfMalo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfMaloKeyPressed(evt);
            }
        });
        jpSuperior.add(jtfMalo, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, 70, 30));

        jtfRegular.setText(" ");
        jtfRegular.setNextFocusableComponent(jtfBueno);
        jtfRegular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfRegularKeyPressed(evt);
            }
        });
        jpSuperior.add(jtfRegular, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, 70, 30));

        jtfBueno.setText(" ");
        jtfBueno.setNextFocusableComponent(jtfExcelente);
        jtfBueno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfBuenoKeyPressed(evt);
            }
        });
        jpSuperior.add(jtfBueno, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 100, 70, 30));

        jtfExcelente.setText(" ");
        jtfExcelente.setNextFocusableComponent(jtfFinal);
        jtfExcelente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfExcelenteKeyPressed(evt);
            }
        });
        jpSuperior.add(jtfExcelente, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 70, 30));

        jtfFinal.setText(" ");
        jtfFinal.setNextFocusableComponent(jtfInicialQ);
        jtfFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfFinalKeyPressed(evt);
            }
        });
        jpSuperior.add(jtfFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 70, 70, 30));

        jlInicialQ.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlInicialQ.setText("Q-Inicial");
        jpSuperior.add(jlInicialQ, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, 60, 30));

        jlGamma.setText("Gamma:");
        jpSuperior.add(jlGamma, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 60, 20));

        jCEpsilon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9" }));
        jCEpsilon.setEnabled(false);
        jCEpsilon.setNextFocusableComponent(jCGamma);
        jCEpsilon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCEpsilonActionPerformed(evt);
            }
        });
        jpSuperior.add(jCEpsilon, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 50, 20));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jpSuperior.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, 10, 120));
        jpSuperior.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 210, 10));

        jlConfigPoliticas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jlConfigPoliticas.setText("Configuración de la Política:");
        jpSuperior.add(jlConfigPoliticas, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 200, 20));

        getContentPane().add(jpSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 140));

        jpTablero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpTablero.setLayout(new java.awt.GridLayout(1, 0));
        getContentPane().add(jpTablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 560, 560));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextCantidadEpisodios.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextCantidadEpisodios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextCantidadEpisodiosActionPerformed(evt);
            }
        });
        jPanel1.add(jTextCantidadEpisodios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 30));

        jBEntrena.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/control_right.png"))); // NOI18N
        jBEntrena.setText("Entrenar");
        jBEntrena.setEnabled(false);
        jBEntrena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBEntrenaActionPerformed(evt);
            }
        });
        jPanel1.add(jBEntrena, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 140, 50));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Ciclos de entrenamiento");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 150, 40));

        jBAvanza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/control_double_right.png"))); // NOI18N
        jBAvanza.setText("Avanzar");
        jBAvanza.setEnabled(false);
        jBAvanza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAvanzaActionPerformed(evt);
            }
        });
        jPanel1.add(jBAvanza, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 140, 50));

        jLabelItera.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelItera.setText("Iteraciones Realizadas");
        jPanel1.add(jLabelItera, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 5, -1, 30));

        jLabelContador.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jLabelContador, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 140, 30));

        jbEmpezarDeNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow_circle_right.png"))); // NOI18N
        jbEmpezarDeNuevo.setText("Reiniciar");
        jbEmpezarDeNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEmpezarDeNuevoActionPerformed(evt);
            }
        });
        jPanel1.add(jbEmpezarDeNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 290, 50));

        jbDetener.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/control_pause.png"))); // NOI18N
        jbDetener.setText("Detener");
        jbDetener.setEnabled(false);
        jbDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDetenerActionPerformed(evt);
            }
        });
        jPanel1.add(jbDetener, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 140, 50));

        jbReanudar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/control_right.png"))); // NOI18N
        jbReanudar.setText("Continuar");
        jbReanudar.setEnabled(false);
        jbReanudar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbReanudarActionPerformed(evt);
            }
        });
        jPanel1.add(jbReanudar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 140, 50));

        jMatrizQ.setText("Matriz Q");
        jMatrizQ.setEnabled(false);
        jMatrizQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMatrizQActionPerformed(evt);
            }
        });
        jPanel1.add(jMatrizQ, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 140, 50));

        jBAvanza1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/control_stop_right.png"))); // NOI18N
        jBAvanza1.setText("Avanzar");
        jBAvanza1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAvanza1ActionPerformed(evt);
            }
        });
        jPanel1.add(jBAvanza1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 140, 50));

        jBGrafica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Line-chart32.png"))); // NOI18N
        jBGrafica.setText("Grafica");
        jBGrafica.setEnabled(false);
        jBGrafica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGraficaActionPerformed(evt);
            }
        });
        jPanel1.add(jBGrafica, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 140, 50));

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 310, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Iniciar Entrenamiento");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 310, 10));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Avanzar hacia el Objetivo");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Botones Adicionales");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 110, 30));

        jBBorrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/Trash-can32.png"))); // NOI18N
        jBBorrar.setText("Borrar Camino");
        jBBorrar.setEnabled(false);
        jBBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBBorrarActionPerformed(evt);
            }
        });
        jPanel1.add(jBBorrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 140, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 220, 310, 460));

        jlEstadoInicial.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jlEstadoInicial.setText("*Advertencia: Debe elegir un estado \"Inicial\" ");
        getContentPane().add(jlEstadoInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 170, 320, -1));

        jlAusenciaEstadoFinal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jlAusenciaEstadoFinal.setText("*Advertencia: Debe elegir un estado \"Final\"");
        getContentPane().add(jlAusenciaEstadoFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 190, -1, 20));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public final void visibleConfigPoliticas(Boolean visible){
        jlRecompensas.setVisible(visible);
              
        jrbEGreedy.setVisible(visible);
        jrbSoftMax.setVisible(visible);
    }
    private void jbGenerarTableroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbGenerarTableroActionPerformed

        jTextCantidadEpisodios.setText("");
        jLabelContador.setText("");
        jrbEGreedy.setSelected(true);
        jrbSoftMax.setSelected(false);
        if(jrbAuto.isSelected()){
            String aux = (String)jcbDim.getSelectedItem();
            switch(aux){
                case "6x6":this.cargarTableroAleatorio(6);
                    jTextCantidadEpisodios.setText("1500");
                    break;
                case "7x7":this.cargarTableroAleatorio(7);
                    jTextCantidadEpisodios.setText("3000");
                    break;
                case "8x8":this.cargarTableroAleatorio(8);
                    jTextCantidadEpisodios.setText("5000");
                    break;
                case "9x9":this.cargarTableroAleatorio(9);
                    jTextCantidadEpisodios.setText("8000");
                    break;
                case "10x10":this.cargarTableroAleatorio(10);
                    jTextCantidadEpisodios.setText("10000");
                    break;             
            } 
        }else{
            if(jrbManual.isSelected()){
               String aux = (String)jcbDim.getSelectedItem();
               switch(aux){
                    case "6x6":this.cargarTableroManual(6);
                    jTextCantidadEpisodios.setText("750");
                    break;
                case "7x7":this.cargarTableroManual(7);
                    jTextCantidadEpisodios.setText("1000");
                    break;
                case "8x8":this.cargarTableroManual(8);
                    jTextCantidadEpisodios.setText("1250");
                    break;
                case "9x9":this.cargarTableroManual(9);
                    jTextCantidadEpisodios.setText("1500");
                    break;
                case "10x10":this.cargarTableroManual(10);
                    jTextCantidadEpisodios.setText("2000");
                    break;
               }    
           
            }
            
        }
    }//GEN-LAST:event_jbGenerarTableroActionPerformed

    private void jrbManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbManualActionPerformed

        jrbManual.setSelected(true);
        jrbAuto.setSelected(false);
        jbGenerarTablero.setEnabled(true);
       
    }//GEN-LAST:event_jrbManualActionPerformed

    private void jrbAutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbAutoActionPerformed

        jCGamma.setEnabled(true);
        jrbManual.setSelected(false);
        jrbAuto.setSelected(true);
        jbGenerarTablero.setEnabled(true);
        
    }//GEN-LAST:event_jrbAutoActionPerformed

    private void jcbDimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbDimActionPerformed

    private void jrbEGreedyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbEGreedyMouseClicked

//        jtfEpsTau.setVisible(true);
        jlEpsilon.setVisible(true);
        jlTau.setVisible(false);
        jrbEGreedy.setSelected(true);
        jrbSoftMax.setSelected(false);
        
    }//GEN-LAST:event_jrbEGreedyMouseClicked

    private void jrbSoftMaxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbSoftMaxMouseClicked

//        jtfEpsTau.setVisible(true);
        jlTau.setVisible(true);
        jlEpsilon.setVisible(false);
        jrbSoftMax.setSelected(true);
        jrbEGreedy.setSelected(false);
    }//GEN-LAST:event_jrbSoftMaxMouseClicked

    private void jBEntrenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBEntrenaActionPerformed
        
        // Se eliminan los eventos de cada estado y se agrega a cada boton el
        // evento para setear el estado inicial
        MainWindow.jlEstadoInicial.setVisible(true);
        
        
        jBAvanza.setEnabled(false);//desactivo el boton de Avanzar
        jbDetener.setEnabled(true);//Activo el boton de Detener
        jBGrafica.setEnabled(true);//Activo el boton de Grafico
        jMatrizQ.setEnabled(true);//Activo el boton de Matriz Q
        
        String userdata = jTextCantidadEpisodios.getText().trim(); //obtengo el numero de episodios del label
       
        int val;//parseo los valores a int y capturo las Excepciones
            
                try
                {
                   val = Integer.parseInt(userdata);
                   if(val<0){
                     JOptionPane.showMessageDialog(this,"Valores Negativos Invalidos para Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                     jTextCantidadEpisodios.setText(Integer.toString(Configuraciones.cantEpisodios));
                     val = Configuraciones.cantEpisodios;  
                   }
                }
                catch (NumberFormatException nfe)
                {
                   JOptionPane.showMessageDialog(this,"Valores Invalidos de Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                   jTextCantidadEpisodios.setText(Integer.toString(Configuraciones.cantEpisodios));
                   val = Configuraciones.cantEpisodios;
                }              
       
        //fin de parseo
        Configuraciones.setCantEpisodios(val);//seteo los valores en Configuraciones
        //Verifico las banderas para setear la politica
        if (banderaEGreedy==true){
            PoliticaEGreedy politica= new PoliticaEGreedy();
            p=politica;
            System.out.println("POLITICA EGREEDY");
            
        }
        else{
            PoliticaSoftMax politica= new PoliticaSoftMax();
            p=politica;
            
            System.out.println("POLITICA SOFTMAX");           
        }
        //fin de la verificaciones  
        jBEntrena.setEnabled(false);//desabilito el boton de Entrenamiento 
        
        iniciarEntrenamiento();//llama al hilo de entrenamiento
        
        this.agregarEvEstadoInicial();
    }//GEN-LAST:event_jBEntrenaActionPerformed

    public void eliminarEventos(){          
        for (int i = 0; i < Configuraciones.getDimension(); i++) {
            for (int j = 0; j < Configuraciones.getDimension(); j++) {
                int indice=(i*Configuraciones.getDimension()) + j;
                Component componente= jpTablero.getComponent(indice);
                if(componente.getClass() == JButton.class){
                    componente.removeMouseListener(mClickedCambiarEstados);
                    componente.removeMouseWheelListener(mWheelMovedCambiarEstados);
                }            
            }
        }       
    }
    
      public void agregarEvEstadoInicial(){ 
        
        MainWindow.estadoInicial = false;
        MainWindow.jlEstadoInicial.setVisible(true);
        MainWindow.jBAvanza.setEnabled(false);
            
        for (int i = 0; i < Configuraciones.getDimension(); i++) {
            for (int j = 0; j < Configuraciones.getDimension(); j++) {
                int indice=(i*Configuraciones.getDimension()) + j;
                Component componente= jpTablero.getComponent(indice);
                if(componente.getClass() == JButton.class){
                    componente.addMouseListener(mClickedEstadoInicial);
                }    
            }
        }       
    }
    
 
    private void jBAvanzaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAvanzaActionPerformed
        //recorriendo para ver mejor camino
        //obtengo la matriz Q del ultimo episodio
        QMat matQ = episodios[Configuraciones.cantEpisodios-1].getMatrizQ();    
        
        QMat matrizQ = new QMat(matQ.matQ.clone()); 
        

        
        // setea el estado inicial
        Estado estadoInicial= null;
        //recorre todo el arreglo de botones hasta encontrar el estado Inicial
        for (int i = 0; i < Configuraciones.getDimension(); i++) {
            for (int j = 0; j < Configuraciones.getDimension(); j++) {
                int indice=(i*Configuraciones.getDimension()) + j;
                Component componente= jpTablero.getComponent(indice);
                if(componente.getClass() == JButton.class){
                    JButton s = (JButton)componente;
                    if(("I".equals(s.getText())) | ("INICIAL".equals(s.getText()))){
                        estadoInicial= matrizQ.getEstado(i,j); 
                        
                        MainWindow.ii = i; // aca atrapo la posicion del estado Inicial
                        MainWindow.jj = j;
                    }
                }     
            }
        }        

//        Estado banderaEstadoFinal= matrizQ.getEstado(Configuraciones.getFilaF(),Configuraciones.getColF());
        
        // setea el estado final
        Estado estadoFinal= null;
        //recorre todo el arreglo de botones hasta encontrar el estado Final
        for (int i = 0; i < Configuraciones.getDimension(); i++) {
            for (int j = 0; j < Configuraciones.getDimension(); j++) {
                int indice=(i*Configuraciones.getDimension()) + j;
                Component componente= jpTablero.getComponent(indice);
                if(componente.getClass() == JButton.class){
                    JButton s = (JButton)componente;
                    if(("F".equals(s.getText())) | ("FINAL".equals(s.getText()))){
                        estadoFinal= matrizQ.getEstado(i,j);         
                    }
                }     
            }
        }        
        
        System.out.println(estadoInicial);
        System.out.println("Movimientos:");

        
        //habilitar el estado inical
        int ind= (estadoInicial.getPosI()*Configuraciones.getDimension() ) +estadoInicial.getPosJ();
        JButton botonInicial = (JButton) jpTablero.getComponent(ind);
        botonInicial.setBorder(BorderFactory.createLineBorder(Color.magenta,4));
        
        //seteo valores necesarios para el bucle
        int cont=0;
        //seteo un tope en funcion a la dimension del tablero
        int x = Configuraciones.getDimension();
        int y;
        if(jCGamma.getSelectedIndex()<0.5){
            y = 4;         
        }
        else{
            y = 3;
        }
        int tope = (int) Math.pow(x, y);
        
        int val= Integer.parseInt(jTextCantidadEpisodios.getText().trim()); //Controlo que el tope no sea menor que el numero de episodios del label
        if(val > tope){
            tope=val;
        }
        

         
        
        //mientras estado actual distinto de estado final recorro la matriz Q y cambio el color
        while(!estadoInicial.equals(estadoFinal)&&(cont<tope)){
                
              Estado estadoProximo = estadoInicial.accionDeMaximoValor().getEstadoDestino();
              System.out.println(estadoProximo);//imprime por consola el estado proximo
              int indice= (estadoProximo.getPosI()*Configuraciones.getDimension() ) +estadoProximo.getPosJ();//genero un indice porque los botones son un arreglo y Q una matriz
              JButton boton = (JButton) jpTablero.getComponent(indice);
              boton.setBorder(BorderFactory.createLineBorder(Color.magenta,4));//cambia de color el camino
              estadoInicial= estadoProximo;//avanzo de estado
              cont++;
         }
        //imprime una ventana si el agente se bloqueo
        System.out.println(cont);
        if (cont==tope){
            if(jrbEGreedy.isSelected()){
                JOptionPane.showMessageDialog(this,"Agente Bloqueado o con poco entrenamiento, pruebe con configuraciones de politica mayores, o con mayor numero de episodios","Error",JOptionPane.WARNING_MESSAGE);
            }
            else{
               JOptionPane.showMessageDialog(this,"Agente Bloqueado o con poco entrenamiento","Error",JOptionPane.WARNING_MESSAGE); 
            }
            
        }
        jBBorrar.setEnabled(true);
    }//GEN-LAST:event_jBAvanzaActionPerformed
    private void jTextCantidadEpisodiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextCantidadEpisodiosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextCantidadEpisodiosActionPerformed

    private void jrbEGreedyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbEGreedyActionPerformed
        
        jCGamma.setVisible(true);
        jCGamma.setEnabled(true);
        jCEpsilon.setVisible(true);
        jCEpsilon.setEnabled(true);
        jlEpsilon.setVisible(true);
        jCTau.setVisible(false);
        jlTau.setVisible(false);
        jCEpsilon.setFocusable(true);
    }//GEN-LAST:event_jrbEGreedyActionPerformed

    private void jBGraficaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGraficaActionPerformed
        jpSuperior.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jpTablero.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jPanel1.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        LineChart grafico = new LineChart("Valores de matriz Q ",episodios);
        grafico.setSize(800, 600);
        grafico.setLocation(this.getLocation());
        grafico.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        grafico.setResizable(true);
        grafico.setVisible(true);
        
        jpSuperior.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        jpTablero.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        jPanel1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jBGraficaActionPerformed

    public void enabledJPSuperior(Boolean flag){
        
        //Primer cuadrante
        jlConfig.setEnabled(flag);
        jlDim.setEnabled(flag);
        jlManOAlea.setEnabled(flag);
        
        jcbDim.setEnabled(flag);
        jrbManual.setEnabled(flag);
        jrbAuto.setEnabled(flag);  
        
        //Segundo cuadrante
        jlGammaTitulo.setEnabled(flag);
        jlEpsilon.setEnabled(flag);
        jlTau.setEnabled(false); //Por defecto
        jlGamma.setEnabled(flag);
        
        jCEpsilon.setEnabled(flag);
        jCTau.setEnabled(false); // Falso por defecto
        jCGamma.setEnabled(flag);
        
        //Tercer Cuadrante
        jlRecompensas.setEnabled(flag);
        jlMalo.setEnabled(flag);
        jlRegular.setEnabled(flag);
        jlbueno.setEnabled(flag);
        jlExcelente.setEnabled(flag);
        jlFinal.setEnabled(flag);
        jlInicialQ.setEnabled(flag);
        
        jtfMalo.setEnabled(flag);
        jtfRegular.setEnabled(flag);
        jtfBueno.setEnabled(flag);
        jtfExcelente.setEnabled(flag);
        jtfFinal.setEnabled(flag);
        jtfInicialQ.setEnabled(flag);
        
        //Quinto Cuadrante
        jbGenerarTablero.setEnabled(flag);
        jbConfirmar.setEnabled(false); //Por defecto
        
    }
    
    public void setEnabledJPSuperior(boolean flag){
        //Primer cuadrante
        jlConfig.setEnabled(flag);
        jlDim.setEnabled(flag);
        jlManOAlea.setEnabled(flag);
        
        jcbDim.setEnabled(flag);
        jrbManual.setEnabled(flag);
        jrbAuto.setEnabled(flag);  
        
        //Segundo cuadrante
        jlGammaTitulo.setEnabled(flag);
        jlEpsilon.setEnabled(flag);
        jlTau.setEnabled(flag);
        jlGamma.setEnabled(flag);
        
        jCEpsilon.setEnabled(flag);
        jCTau.setEnabled(false); // Falso por defecto
        jCGamma.setEnabled(flag);
        
        //Tercer Cuadrante
        jlRecompensas.setEnabled(flag);
        jlMalo.setEnabled(flag);
        jlRegular.setEnabled(flag);
        jlbueno.setEnabled(flag);
        jlExcelente.setEnabled(flag);
        jlFinal.setEnabled(flag);
        jlInicialQ.setEnabled(flag);
        
        jtfMalo.setEnabled(flag);
        jtfRegular.setEnabled(flag);
        jtfBueno.setEnabled(flag);
        jtfExcelente.setEnabled(flag);
        jtfFinal.setEnabled(flag);
        jtfInicialQ.setEnabled(flag);
        
        //Quinto Cuadrante
        jbGenerarTablero.setEnabled(flag);
        jbConfirmar.setEnabled(false); //Por defecto
    }
    
    private void jbConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConfirmarActionPerformed

        this.eliminarEventos();
        
        jbEmpezarDeNuevo.setVisible(true);
        
        this.setEnabledJPSuperior(false);
        jPanel1.setVisible(true);
        if (jrbEGreedy.isSelected()){
            banderaEGreedy=true;
            String epsilon = (String)jCEpsilon.getSelectedItem();
            Double valorEp = Double.parseDouble(epsilon);
            Configuraciones.setEpsilon(valorEp);
            System.out.println("Epsilon "+Configuraciones.getEpsilon());
        }
        if (jrbSoftMax.isSelected()){

            String tau = (String)jCTau.getSelectedItem();
            Double valorTau = Double.parseDouble(tau);
            Configuraciones.setTau(valorTau);
            System.out.println("Tau "+Configuraciones.getTau());
            
        }
        
        String malo=(jtfMalo.getText().trim()); 
        double valorMalo;
                     try
                    {
                       valorMalo = Double.parseDouble(malo);
                       if(valorMalo<0){
                            JOptionPane.showMessageDialog(this,"Valores Negativos Invalidos para Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                            jtfMalo.setText(Double.toString(Configuraciones.getValorMalo()));
                            valorMalo = Configuraciones.getValorMalo();
                        }
                    }
                    catch (NumberFormatException nfe)
                    {
                       JOptionPane.showMessageDialog(this,"Valores Invalidos de Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                       jtfMalo.setText(Double.toString(Configuraciones.getValorMalo()));
                       valorMalo = Configuraciones.getValorMalo();
                    }
        
        Configuraciones.setValorMalo(valorMalo);
        
        String regular=(jtfRegular.getText().trim()); 
        double valorReg;
        
       
                    try
                    {
                       valorReg = Double.parseDouble(regular);
                       if(valorReg<0){
                            JOptionPane.showMessageDialog(this,"Valores Negativos Invalidos para Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                            jtfRegular.setText(Double.toString(Configuraciones.getValorNeutro()));
                            valorReg = Configuraciones.getValorNeutro();
                        }
                       
                    }
                    catch (NumberFormatException nfe)
                    {
                       JOptionPane.showMessageDialog(this,"Valor incorrecto de casillero Neutro, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                       jtfRegular.setText(Double.toString(Configuraciones.getValorNeutro()));
                       valorReg = Configuraciones.getValorNeutro();
                    }
                     
       
        Configuraciones.setValorNeutro(valorReg);
        
        String bueno=(jtfBueno.getText().trim()); 
        double valorBueno;
       
                     try
                    {
                       valorBueno = Double.parseDouble(bueno);
                       if(valorBueno<0){
                            JOptionPane.showMessageDialog(this,"Valores Negativos Invalidos para Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                            jtfBueno.setText(Double.toString(Configuraciones.getValorBueno()));
                            valorBueno = Configuraciones.getValorBueno(); 
                        }
                    }
                    catch (NumberFormatException nfe)
                    {
                       JOptionPane.showMessageDialog(this,"Valor incorrecto de casillero bueno, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                       jtfBueno.setText(Double.toString(Configuraciones.getValorBueno()));
                       valorBueno = Configuraciones.getValorBueno();
                    }           
        
        
        Configuraciones.setValorBueno(valorBueno);
        
        String excelente=(jtfExcelente.getText().trim()); 
        double valorExcelente;
        
                     try
                    {
                       valorExcelente = Double.parseDouble(excelente);
                       if(valorExcelente<0){
                            JOptionPane.showMessageDialog(this,"Valores Negativos Invalidos para Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                            jtfExcelente.setText(Double.toString(Configuraciones.getValorExcelente()));
                            valorExcelente = Configuraciones.getValorExcelente();  
                       }   
                    }
                    catch (NumberFormatException nfe)
                    {
                       JOptionPane.showMessageDialog(this,"Valor incorrecto de casillero excelente, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                       jtfExcelente.setText(Double.toString(Configuraciones.getValorExcelente()));
                       valorExcelente = Configuraciones.getValorExcelente();
                    }                  
        
        Configuraciones.setValorExcelente(valorExcelente);
        
        String fin=(jtfFinal.getText().trim()); 
        double valorFinal;
        
                     try
                    {
                       valorFinal = Double.parseDouble(fin);
                       if(valorFinal<0){
                            JOptionPane.showMessageDialog(this,"Valores Negativos Invalidos para Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                            jtfFinal.setText(Double.toString(Configuraciones.getValorFinal()));
                            valorFinal = Configuraciones.getValorFinal();
                       }  
                       
                    }
                    catch (NumberFormatException nfe)
                    {
                       JOptionPane.showMessageDialog(this,"Valor incorrecto de casillero final, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                       jtfFinal.setText(Double.toString(Configuraciones.getValorFinal()));
                       valorFinal = Configuraciones.getValorFinal();
                    }                
        
        Configuraciones.setValorFinal(valorFinal);
        
        String inicio=(jtfInicialQ.getText().trim()); 
        int valorInicio;
        
                     try
                    {
                       valorInicio = Integer.parseInt(inicio);
                       if(valorInicio<0){
                            JOptionPane.showMessageDialog(this,"Valores Negativos Invalidos para Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                            jtfInicialQ.setText(Double.toString(Configuraciones.getValorPorDefectoMatQ()));
                            valorInicio = Configuraciones.getValorPorDefectoMatQ();
                           
                       }
                    }
                    catch (NumberFormatException nfe)
                    {
                       JOptionPane.showMessageDialog(this,"Valor incorrecto de casillero Q-Inicial, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                       jtfInicialQ.setText(Double.toString(Configuraciones.getValorPorDefectoMatQ()));
                       valorInicio = Configuraciones.getValorPorDefectoMatQ();
                    }
                     
        
        Configuraciones.setValorPorDefectoMatQ(valorInicio);
        
            String gamma = (String)jCGamma.getSelectedItem();
            Double valorGamma = Double.parseDouble(gamma);
            Configuraciones.setGamma(valorGamma);
            System.out.println("Gamma "+Configuraciones.getGamma());
        
        jBEntrena.setEnabled(true);      
    }//GEN-LAST:event_jbConfirmarActionPerformed

    private void jrbSoftMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbSoftMaxActionPerformed
        
        jCGamma.setVisible(true);
        jCGamma.setEnabled(true);
        jlEpsilon.setVisible(false);
        jCEpsilon.setVisible(false);
        jCTau.setVisible(true);
        jCTau.setEnabled(true);
        jCTau.setFocusable(true);
    }//GEN-LAST:event_jrbSoftMaxActionPerformed

    private void jCGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCGammaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCGammaActionPerformed

    private void jCEpsilonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCEpsilonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCEpsilonActionPerformed

    private void jtfMaloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfMaloKeyPressed

//        if(evt.KEY_PRESSED == java.awt.event.KeyEvent.VK_TAB) {
//            jtfRegular.setFocusable(true);
//        }  
    }//GEN-LAST:event_jtfMaloKeyPressed

    private void jtfRegularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfRegularKeyPressed
//        if(evt.KEY_PRESSED == java.awt.event.KeyEvent.VK_TAB) {
//            jtfBueno.setFocusable(true);
//        }
    }//GEN-LAST:event_jtfRegularKeyPressed

    private void jtfBuenoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfBuenoKeyPressed
        
//        if(evt.KEY_PRESSED == java.awt.event.KeyEvent.VK_TAB) {
//            jtfExcelente.setFocusable(true);
//        }
    }//GEN-LAST:event_jtfBuenoKeyPressed

    private void jtfExcelenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfExcelenteKeyPressed

//        if(evt.KEY_PRESSED == java.awt.event.KeyEvent.VK_TAB) {
//            jtfFinal.setFocusable(true);
//        }
    }//GEN-LAST:event_jtfExcelenteKeyPressed

    private void jtfFinalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFinalKeyPressed

//        if(evt.KEY_PRESSED == java.awt.event.KeyEvent.VK_TAB) {
//            jtfInicialQ.setFocusable(true);
//        }
    }//GEN-LAST:event_jtfFinalKeyPressed

    private void jbEmpezarDeNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEmpezarDeNuevoActionPerformed

        jPanel1.setVisible(false);
        jpSuperior.setEnabled(true);
        jBAvanza.setEnabled(false);
        contadorEpisodios=0;
        EpisodioStop=0;
        TotalEpisodios=0;
        jBBorrar.setEnabled(false);
        reinicio=true;
        jrbEGreedy.setSelected(true);
        jrbSoftMax.setSelected(false);
        
        this.enabledJPSuperior(true);
        MainWindow.vistaConfigPoliticas(true);
//        VentanaPrincipal.jlAusenciaEstadoFinal.setVisible(false);
//        VentanaPrincipal.jlInicialNoPared.setVisible(false);
        jPanel1.setVisible(true);

        MainWindow.jlAusenciaEstadoFinal.setVisible(false);
        MainWindow.jlEstadoInicial.setVisible(false);
        MainWindow.estadoInicial = true;
        MainWindow.banderaEstadoFinal = true;
        
        jpTablero.setVisible(false);
        jpTablero.removeAll();
        jpTablero.setLayout(new GridLayout(Configuraciones.getDimension(),Configuraciones.getDimension()));
        
        MainWindow.vistaConfigPoliticas(true);
        MainWindow.jlAusenciaEstadoFinal.setVisible(false);
        jPanel1.setVisible(false);
        jCGamma.setVisible(true);
        jCTau.setVisible(false);
        jlEstadoInicial.setVisible(false);
        jbGenerarTablero.setEnabled(true);
        jpTablero.setVisible(true);
        jpTablero.repaint();
        MainWindow.jbConfirmar.setEnabled(true);
        
        
    }//GEN-LAST:event_jbEmpezarDeNuevoActionPerformed

    private void jbDetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDetenerActionPerformed
        //Activa la bandera de detener para que se detenga el entrenamiento
        detener=true;
        //Habilito y desabilito los botones Reanudar y Detener respectivamente
        jbReanudar.setEnabled(true);
        jbDetener.setEnabled(false);
        //almaceno el numero del episodio donde se detuvo
        EpisodioStop=contadorEpisodios;
        
        
    }//GEN-LAST:event_jbDetenerActionPerformed

    private void jbReanudarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbReanudarActionPerformed
        //desabilito la bandera para que continue el entrenamiento
        jbDetener.setEnabled(true);
      
        reanudarEntrenamiento();//llama al hilo de entrenamiento
       
    }//GEN-LAST:event_jbReanudarActionPerformed

    private void jMatrizQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMatrizQActionPerformed
       // QVentana QVent= new QVentana();
        MovementsWindow vMov= new MovementsWindow();
        vMov.setVisible(true);
        QWindow qWindow= new QWindow(episodios[contadorEpisodios-1].getMatrizQ());
        qWindow.setSize(this.getSize());
        qWindow.setLocation(this.getLocation());
        qWindow.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        qWindow.setVisible(true);
    }//GEN-LAST:event_jMatrizQActionPerformed

    private void jBAvanza1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAvanza1ActionPerformed
        //habilito el boton de Matriz Q
        jMatrizQ.setEnabled(true);
        
        if (avanzoUnaVez==0){// si no se avanzo nunca, hago lo mismo que el boton de entrenamiento
            //Inicio entrenamiento como la 1ra vez
            jBAvanza.setEnabled(false);
            jbDetener.setEnabled(true);
            jBGrafica.setEnabled(true);

            String userdata = jTextCantidadEpisodios.getText().trim();

            int val;
            if (Integer.parseInt(userdata)>0){            
                    try
                    {
                       val = Integer.parseInt(userdata);
                    }
                    catch (NumberFormatException nfe)
                    {
                       JOptionPane.showMessageDialog(this,"Valores Invalidos de Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                       jTextCantidadEpisodios.setText(Integer.toString(Configuraciones.cantEpisodios));
                       val = Configuraciones.cantEpisodios;
                    }              
            }
            else{
                JOptionPane.showMessageDialog(this,"Valores Invalidos de Entenamiento, se cargará un numero de ciclos por default","Error",JOptionPane.WARNING_MESSAGE);
                jTextCantidadEpisodios.setText(Integer.toString(Configuraciones.cantEpisodios));
                val = Configuraciones.cantEpisodios;
            }
            Configuraciones.setCantEpisodios(val);

            if (banderaEGreedy==true){
                PoliticaEGreedy politica= new PoliticaEGreedy();
                p=politica;
                System.out.println("POLITICA EGREEDY");

            }
            else{
                PoliticaSoftMax politica= new PoliticaSoftMax();
                p=politica;

                System.out.println("POLITICA SOFTMAX");           
            }

            jBEntrena.setEnabled(false);


            iniciarEntrenamiento();//llama al hilo de entrenamiento

            //Detengo
            detener=true;
            jbReanudar.setEnabled(true);
            jbDetener.setEnabled(false);
            EpisodioStop=contadorEpisodios;
            //incrementeo el contador para que reanude de a uno
            avanzoUnaVez++;
        }
        else{//reanudo entrenamiento de a uno
            reanudarEntrenamientoUnaVez();
        }
    }//GEN-LAST:event_jBAvanza1ActionPerformed

    private void jBBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBBorrarActionPerformed
        for (int i = 0; i < Configuraciones.getDimension(); i++) {
            for (int j = 0; j < Configuraciones.getDimension(); j++) {
                int indice=(i*Configuraciones.getDimension()) + j;
                Component componente= jpTablero.getComponent(indice);
                if(componente.getClass() == JButton.class){
                    JButton s = (JButton)componente;
                    s.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));//cambia de color el camino
                    if(("I".equals(s.getText())) | ("INICIAL".equals(s.getText()))){
                        s.setBackground(MainWindow.jbAnterior.getBackground());
                        s.setText(MainWindow.jbAnterior.getText());
                        MainWindow.estadoInicial = false;
                        MainWindow.jlEstadoInicial.setVisible(true);
                        
                    }
                } 
                
            }
        
        }
        MainWindow.jBAvanza.setEnabled(false);
        //this.agregarEvEstadoInicial();
    }//GEN-LAST:event_jBBorrarActionPerformed

    private RMat obtenerRdesdePantalla(){
        int dimension= 0;
        String aux = (String)jcbDim.getSelectedItem();
        switch(aux){
            case "6x6":dimension= 6;
                break;
            case "7x7":dimension= 7;
                break;
            case "8x8":dimension= 8;
                break;
            case "9x9":dimension= 9;
                break;
            case "10x10":dimension=10;
                break;
        }    
        Configuraciones.setDimension(dimension);
        
        RMat matR= new RMat(Configuraciones.getDimension());
//        int indice=(Configuraciones.getFilaI()*matR.dimension) + Configuraciones.getColI();
//        if(JButton.getComponent(indice).getBackground().equals(BLACK)){
//        
//        }
        for(int i=0;i<matR.dimension;i++){
            for(int j=0;j<matR.dimension;j++){
                int indice=(i*matR.dimension) + j;
                Component componente= jpTablero.getComponent(indice);
                if(componente.getClass()==JButton.class){
                JButton boton= (JButton) componente;
                String txt= boton.getText();
                Color fondo= boton.getBackground();
                
                //cargo los valores a la matriz R dependiendo de de las letras o colores de los botones del tablero
                if(txt.equalsIgnoreCase("Malo") || txt.equalsIgnoreCase("M")){
                    matR.mat[i][j]=Configuraciones.getValorMalo();
                }
               
                if("Bueno".equals(txt) || "B".equals(txt)){
                    matR.mat[i][j]=Configuraciones.getValorBueno();
                }
                if("Excelente".equals(txt) || "E".equals(txt)){
                    matR.mat[i][j]=Configuraciones.getValorExcelente();
                }
                if("Malo".equals(txt) || "M".equals(txt)){
                    matR.mat[i][j]=Configuraciones.getValorMalo();
                }
                if(fondo == Color.WHITE){
                    matR.mat[i][j]=Configuraciones.getValorNeutro();
                }
                
                if(fondo == Color.BLACK){
                    matR.mat[i][j]=Configuraciones.getValorPared();
                }
                
                if("FINAL".equals(txt) || "F".equals(txt)){
                    matR.mat[i][j]=Configuraciones.getValorFinal();
                    Configuraciones.setFinal(i,j);
                }
                if("INICIAL".equals(txt) || "I".equals(txt)){
                    matR.mat[i][j]=Configuraciones.getValorNeutro();
                    Configuraciones.setInicial(i, j);
                    
                }
                }
            }
        }
        return matR;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBAvanza;
    private javax.swing.JButton jBAvanza1;
    private javax.swing.JButton jBBorrar;
    private javax.swing.JButton jBEntrena;
    private javax.swing.JButton jBGrafica;
    private javax.swing.JButton jButton1;
    public static javax.swing.JComboBox jCEpsilon;
    public static javax.swing.JComboBox jCGamma;
    public static javax.swing.JComboBox jCTau;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabelContador;
    private javax.swing.JLabel jLabelItera;
    private javax.swing.JButton jMatrizQ;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private static javax.swing.JSeparator jSeparator1;
    private static javax.swing.JSeparator jSeparator2;
    private static javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTextField jTextCantidadEpisodios;
    public static javax.swing.JButton jbConfirmar;
    private javax.swing.JButton jbDetener;
    private javax.swing.JButton jbEmpezarDeNuevo;
    private static javax.swing.JButton jbGenerarTablero;
    private javax.swing.JButton jbReanudar;
    private static javax.swing.JComboBox jcbDim;
    public static javax.swing.JLabel jlAusenciaEstadoFinal;
    private static javax.swing.JLabel jlConfig;
    public static javax.swing.JLabel jlConfigPoliticas;
    private static javax.swing.JLabel jlDim;
    public static javax.swing.JLabel jlEpsilon;
    public static javax.swing.JLabel jlEstadoInicial;
    public static javax.swing.JLabel jlExcelente;
    public static javax.swing.JLabel jlFinal;
    public static javax.swing.JLabel jlGamma;
    public static javax.swing.JLabel jlGammaTitulo;
    public static javax.swing.JLabel jlInicialQ;
    public static javax.swing.JLabel jlMalo;
    private static javax.swing.JLabel jlManOAlea;
    private static javax.swing.JLabel jlRecompensas;
    public static javax.swing.JLabel jlRegular;
    public static javax.swing.JLabel jlTau;
    public static javax.swing.JLabel jlbueno;
    private javax.swing.JPanel jpSuperior;
    private javax.swing.JPanel jpTablero;
    private static javax.swing.JRadioButton jrbAuto;
    public static javax.swing.JRadioButton jrbEGreedy;
    private static javax.swing.JRadioButton jrbManual;
    public static javax.swing.JRadioButton jrbSoftMax;
    public static javax.swing.JTextField jtfBueno;
    public static javax.swing.JTextField jtfExcelente;
    public static javax.swing.JTextField jtfFinal;
    public static javax.swing.JTextField jtfInicialQ;
    public static javax.swing.JTextField jtfMalo;
    public static javax.swing.JTextField jtfRegular;
    // End of variables declaration//GEN-END:variables
}
