/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package blocos.view;

import java.awt.*;
import javax.swing.JPanel;

/**
 *
 * @author Leon
 */
public class GridBlocos extends JPanel
{
    private int intTamanhoGrid;
    private Color objCorGrid = new Color(230, 230, 230);
    private VisaoBlocos objVisao;

    private Point objSelecao = new Point();
    private boolean boolSelecaoAtiva = false;

    public void setTamanhoGrid(int novoTamanho)
    {
        intTamanhoGrid = novoTamanho;
    }
    public int getTamanhoGrid()
    {
        return intTamanhoGrid;
    }

    public void setSelecao(Point p, boolean ativar)
    {
        if (p != null)
        {
            objSelecao.x = p.x;
            objSelecao.y = p.y;
        }
        boolSelecaoAtiva = ativar;
    }
    public Point getSelecao()
    {
        if (boolSelecaoAtiva)
            return objSelecao;
        
        return null;
    }

    public GridBlocos (int TamanhoGrid, VisaoBlocos vb)
    {
        super();
        intTamanhoGrid = TamanhoGrid;
        objVisao = vb;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        DesenhaGrid(g);
        objVisao.DesenhaBlocos(g);
        DesenhaMarcadoresSelecao(g);
    }

    private void DesenhaGrid(Graphics g)
    {
        int i;        
        ((Graphics2D) g).setStroke(new BasicStroke(1.f, 1, 1, 1f, new float[] {5, 5}, 0f));
        g.setColor(objCorGrid);
        for (i = 0; i <= getWidth() / intTamanhoGrid; i++)
        {
            g.drawLine(i * intTamanhoGrid, 0, i * intTamanhoGrid, getHeight());
        }
        for (i = 0; i <= getHeight() / intTamanhoGrid; i++)
        {
            g.drawLine(0, i * intTamanhoGrid, getWidth(), i * intTamanhoGrid);
        }
        ((Graphics2D) g).setStroke(new BasicStroke());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
    }

    private void DesenhaMarcadoresSelecao(Graphics g)
    {
        if (boolSelecaoAtiva)
        {
            int intTamanhoMarcador = 6;
            int intGapMarcador = 2;
            g.fillRect(objSelecao.x * intTamanhoGrid + intGapMarcador, objSelecao.y * intTamanhoGrid + intGapMarcador, intTamanhoMarcador, intTamanhoMarcador);
            g.fillRect((objSelecao.x + 1) * intTamanhoGrid - (intTamanhoMarcador + intGapMarcador), objSelecao.y * intTamanhoGrid + intGapMarcador, intTamanhoMarcador, intTamanhoMarcador);
            g.fillRect(objSelecao.x * intTamanhoGrid + intGapMarcador, (objSelecao.y + 1) * intTamanhoGrid - (intTamanhoMarcador + intGapMarcador), intTamanhoMarcador, intTamanhoMarcador);
            g.fillRect((objSelecao.x + 1) * intTamanhoGrid - (intTamanhoMarcador + intGapMarcador), (objSelecao.y + 1) * intTamanhoGrid - (intTamanhoMarcador + intGapMarcador), intTamanhoMarcador, intTamanhoMarcador);

            g.fillRect((int)((objSelecao.x + 0.5) * intTamanhoGrid) - (intTamanhoMarcador / 2), objSelecao.y * intTamanhoGrid + intGapMarcador, intTamanhoMarcador, intTamanhoMarcador);
            g.fillRect(objSelecao.x * intTamanhoGrid + intGapMarcador, (int)((objSelecao.y + 0.5) * intTamanhoGrid) - (intTamanhoMarcador / 2), intTamanhoMarcador, intTamanhoMarcador);
            g.fillRect((int)((objSelecao.x + 0.5) * intTamanhoGrid) - (intTamanhoMarcador / 2), (objSelecao.y + 1) * intTamanhoGrid - (intTamanhoMarcador + intGapMarcador), intTamanhoMarcador, intTamanhoMarcador);
            g.fillRect((objSelecao.x + 1) * intTamanhoGrid - (intTamanhoMarcador + intGapMarcador), (int)((objSelecao.y + 0.5) * intTamanhoGrid) - (intTamanhoMarcador / 2), intTamanhoMarcador, intTamanhoMarcador);
        }
    }
}
