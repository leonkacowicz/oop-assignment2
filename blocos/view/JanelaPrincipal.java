/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package blocos.view;


import blocos.model.Bloco;
import blocos.model.CarregadorBlocos;
import blocos.controller.GerenciadorBlocos;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 *
 * @author Leon
 */
public class JanelaPrincipal extends JFrame implements ActionListener, 
                                                        MouseListener,
                                                        MouseMotionListener,
                                                        MouseWheelListener,
                                                        KeyListener,
                                                        VisaoBlocos
{

    private final String strTitulo = "Blocos Interativos";
    private final int intTamanhoGrid = 90;
    private GroupLayout objLayoutPanelPrincipal;
    private GridBlocos objContentPane;
    private Timer objTimer;
    private GerenciadorBlocos objGerenciadorBlocos;
    private CarregadorBlocos objCarregadorBlocos;

    // Ponto onde se começou a fazer o dragging
    private Point objPtoInicial = new Point();
    private Point objPtoUltSnap = new Point();

    private JMenuItem objMenuRemover;


    private boolean boolCarregandoBloco = false;
    

    public JanelaPrincipal()
    {
        objGerenciadorBlocos = new GerenciadorBlocos(this);
        objCarregadorBlocos = CarregadorBlocos.getCarregadorBlocos();

        setTitle(strTitulo);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void Mostrar()
    {        
        setVisible(true);
        CriarAmbiente();
    }
    public void CriarAmbiente()
    {

        JMenuBar objMenuBar = new JMenuBar();
        JMenu objMenu = new JMenu("Arquivo");

        objMenu.add(new JMenuItem("Novo")).addActionListener(this);
        objMenu.add(new JMenuItem("Abrir...")).addActionListener(this);
        objMenu.add(new JMenuItem("Salvar")).addActionListener(this);
        objMenu.add(new JMenuItem("Salvar como...")).addActionListener(this);
        objMenu.add(new JSeparator());
        objMenu.add(new JMenuItem("Sair")).addActionListener(this);
        objMenuBar.add(objMenu);

        objMenu = new JMenu("Blocos");

        objMenu.addActionListener(this);
        try {
            String [] objNomeBlocos = objCarregadorBlocos.ObterBlocosDisponiveis();
            for (int i = 0; i < objNomeBlocos.length; i++) {
                objMenu.add(new JMenuItem("Inserir " + objNomeBlocos[i])).addActionListener(this);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        
        objMenuRemover = new JMenuItem("Remover");
        objMenu.add(objMenuRemover).addActionListener(this);
        objMenuRemover.setEnabled(false);


        objMenuBar.add(objMenu);
        setJMenuBar(objMenuBar);

        /* Configura o painel principal que será o "canvas" do programa */
        objContentPane = new GridBlocos(intTamanhoGrid, this);
        
        add(objContentPane);
        
        objContentPane.addMouseListener(this);
        objContentPane.addMouseMotionListener(this);
        objContentPane.addMouseWheelListener(this);
        objContentPane.addKeyListener(this);
        objContentPane.requestFocus();


        objTimer = new Timer(40, this);
        objTimer.start();

        /****************************************************************/

        objContentPane.setBackground(Color.WHITE);
        objLayoutPanelPrincipal = new GroupLayout(objContentPane);
        
        objContentPane.setLayout(objLayoutPanelPrincipal);
        objGerenciadorBlocos.NovoArquivo();
    }

    public void MenuArquivoNovo_Click()
    {
        objGerenciadorBlocos.NovoArquivo();
        repaint();        
    }

    public void MenuArquivoAbrir_Click()
    {
        try {
            JFileChooser fc = new JFileChooser("./");
            fc.showOpenDialog(this);
            if (fc.getSelectedFile() != null)
                objGerenciadorBlocos.LerArquivo(fc.getSelectedFile().getPath());
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }
    public void MenuArquivoSalvarComo_Click()
    {
        try
        {
            JFileChooser fc = new JFileChooser("./");
            fc.showSaveDialog(this);
            if (fc.getSelectedFile() != null)
                objGerenciadorBlocos.SalvarArquivo(fc.getSelectedFile().getPath());
            repaint();
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(this, e2);
        }
    }

    @Override
    public void repaint()
    {
        super.repaint();
        if (objContentPane.getSelecao() == null)
        {
            objMenuRemover.setEnabled(false);
        } else {
            objMenuRemover.setEnabled(true);
        }
    }
    public void MenuArquivoSalvar_Click()
    {
        try {
            objGerenciadorBlocos.SalvarArquivo();
        } catch (Exception e) {
            MenuArquivoSalvarComo_Click();
        }
        
    }
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() instanceof JMenuItem)
        {
            JMenuItem objMenuItem = (JMenuItem)e.getSource();
            if (objMenuItem.getText().equalsIgnoreCase("Novo")) MenuArquivoNovo_Click();
            if (objMenuItem.getText().equalsIgnoreCase("Abrir...")) MenuArquivoAbrir_Click();
            if (objMenuItem.getText().equalsIgnoreCase("Salvar")) MenuArquivoSalvar_Click();
            if (objMenuItem.getText().equalsIgnoreCase("Salvar como...")) MenuArquivoSalvarComo_Click();
            if (objMenuItem.getText().equalsIgnoreCase("Sair")) System.exit(0);
            if (objMenuItem.getText().startsWith("Inserir")) MenuBlocoInserir(objMenuItem.getText().substring(8));
            if (objMenuItem.getText().equalsIgnoreCase("Remover")) MenuBlocoRemover();
        } else if (e.getSource() instanceof Timer) {
            objGerenciadorBlocos.ExecutaVarredura();
            
            repaint();
        }
    }
    private void MenuBlocoInserir(String strTipoBloco)
    {
        try {
            Bloco b = objCarregadorBlocos.InstanciaBloco(strTipoBloco, intTamanhoGrid);
            objGerenciadorBlocos.setBloco(0, 0, b);
            this.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }
    public void mouseDragged(MouseEvent e)
    {
        if (e.getSource() == objContentPane)
        {
            if (boolCarregandoBloco)
            {
                int x = e.getX() / intTamanhoGrid;
                int y = e.getY() / intTamanhoGrid;

                if ((x != objPtoUltSnap.x) || (y != objPtoUltSnap.y))
                {
                    if (objGerenciadorBlocos.getBloco(x, y) == null)
                    {
                        objGerenciadorBlocos.setBloco(x, y, objGerenciadorBlocos.getBloco(objPtoUltSnap.x, objPtoUltSnap.y));
                        objGerenciadorBlocos.setBloco(objPtoUltSnap.x, objPtoUltSnap.y, null);

                        objPtoUltSnap.setLocation(x, y);
                        objContentPane.setSelecao(objPtoUltSnap, true);

                        repaint();
                    }
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        
        if (e.getSource() == objContentPane)
        {
            Bloco b;
            int x, y;
            x = e.getX() / intTamanhoGrid;
            y = e.getY() / intTamanhoGrid;
            b = objGerenciadorBlocos.getBloco(x, y);

            x = e.getX() - x * intTamanhoGrid;
            y = e.getY() - y * intTamanhoGrid;

            if (b instanceof Bloco)
            {
                b.EventoMovimentoMouse(x, y);
                repaint();
            }

        }
    }

    public Graphics ObterGraphics(Graphics g, int x, int y)
    {
        /*
         Esta funcao serve para retornar uma parcela do objeto graphics para se
         desenhar um bloco.
        */
        return  g.create(x * intTamanhoGrid, y * intTamanhoGrid, intTamanhoGrid, intTamanhoGrid);
    }

    public void DesenhaBlocos(Graphics g) {
        objGerenciadorBlocos.MostraBlocos(g);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getSource() == objContentPane)
        {
            Bloco b;
            int x, y;
            x = e.getX() / intTamanhoGrid;
            y = e.getY() / intTamanhoGrid;
            b = objGerenciadorBlocos.getBloco(x, y);
            
            x = e.getX() - x * intTamanhoGrid;
            y = e.getY() - y * intTamanhoGrid;
            
            if (b instanceof Bloco)
            {
                b.EventoClique(x, y);
                repaint();
            }
            
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getSource() == objContentPane) {
            int x, y;
            x = e.getX() / intTamanhoGrid;
            y = e.getY() / intTamanhoGrid;

            objPtoInicial.setLocation(x, y);
            objPtoUltSnap.setLocation(objPtoInicial);

            boolCarregandoBloco = (objGerenciadorBlocos.getBloco(x, y) != null);
            if (boolCarregandoBloco)
                objContentPane.setSelecao(new Point(x, y), true);
            else
                objContentPane.setSelecao(null, false);
            repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == objContentPane)
        {
            if (boolCarregandoBloco)
            {
                int x = objPtoUltSnap.x;
                int y = objPtoUltSnap.y;

                if (objGerenciadorBlocos.getBloco(x, y) instanceof Bloco)
                {
                    // Desconecta o bloco dos vizinhos originais
                    if (objPtoInicial.x != x || objPtoInicial.y != y)
                    {

                        // Desconecta os vizinhos originais do bloco
                        objGerenciadorBlocos.DesconectaBloco(x, y);

                        objGerenciadorBlocos.DesconectaBloco(objPtoInicial.x, objPtoInicial.y);



                        // Conecta o bloco e os novos vizinhos
                        objGerenciadorBlocos.ConectaBloco(x, y);
                        repaint();
                    }
                }
            }
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    private void MenuBlocoRemover()
    {
        Point objSelecao = objContentPane.getSelecao();
        if (objSelecao != null)
        {
            objGerenciadorBlocos.DesconectaBloco(objSelecao.x, objSelecao.y);
            objGerenciadorBlocos.setBloco(objSelecao.x, objSelecao.y, null);
            objContentPane.setSelecao(null, false);
        }
        repaint();
    }

    public void DesenhaBlocos() {
        repaint();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getSource() == objContentPane)
        {
            Bloco b;
            int x, y;
            x = e.getX() / intTamanhoGrid;
            y = e.getY() / intTamanhoGrid;
            b = objGerenciadorBlocos.getBloco(x, y);

            x = e.getX() - x * intTamanhoGrid;
            y = e.getY() - y * intTamanhoGrid;

            if (b instanceof Bloco)
            {
                b.EventoRodaMouse(x, y, e.getWheelRotation());
                repaint();
            }

        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        Bloco b;
        Point p = objContentPane.getSelecao();

        if (p instanceof Point)
        {

            b = objGerenciadorBlocos.getBloco(p.x, p.y);
            b.EventoTecla(String.valueOf(e.getKeyChar()));
            b.Atualizar();
            repaint();
        }

    }

    public void keyReleased(KeyEvent e) {}

    public int ObterTamanhoBlocos() {
        return intTamanhoGrid;
    }

}
