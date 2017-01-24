/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package blocos.controller;

import blocos.view.VisaoBlocos;
import blocos.model.*;
import java.awt.Graphics;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 *
 * @author Leon
 */
public class GerenciadorBlocos
{

    private final int NUM_MAX_BLOCOS_LINHA = 100;
    private final int NUM_MAX_BLOCOS_COLUNA = 100;

    private boolean boolArquivoSalvo = true;
    private String strNomeArquivo;
    
    private Bloco objBlocos[][];

    private VisaoBlocos objVisao;
    
    public GerenciadorBlocos(VisaoBlocos v)
    {
        objVisao = v;
    }

    public String getNomeArquivo()
    {
        return strNomeArquivo;
    }
    public boolean isArquivoSalvo()
    {
        return boolArquivoSalvo;
    }
    public void LerArquivo(String strCaminhoArquivo) throws Exception
    {

        /* Comecamos salvando um backup do contexto para caso dê erro na carga do arquivo. */
        Bloco [][] objBackMatrizBlocos = objBlocos;
        boolean boolBackEstadoArquivo = boolArquivoSalvo;

        /* Zeramos o contexto atual e comecamos a carga do arquivo */
        NovoArquivo();
        File objArquivo = new File(strCaminhoArquivo);
        Document objDocumento = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(objArquivo);
        objDocumento.getDocumentElement().normalize();
        NodeList objNodeList = objDocumento.getDocumentElement().getChildNodes();
        try {
            for (int i = 0; i < objNodeList.getLength(); i++)
            {
                if (objNodeList.item(i).getNodeName().equalsIgnoreCase("bloco"))
                {
                    int x;
                    int y;
                    String strTipo;
                    NodeList objNodeListAtributosBloco;
                    NamedNodeMap objAtributosNode = objNodeList.item(i).getAttributes();
                    Map<String, String> objAtributosBloco = new HashMap<String, String>();

                    strTipo = objAtributosNode.getNamedItem("tipo").getNodeValue();
                    x = Integer.valueOf(objAtributosNode.getNamedItem("x").getNodeValue());
                    y = Integer.valueOf(objAtributosNode.getNamedItem("y").getNodeValue());
                    objNodeListAtributosBloco = objNodeList.item(i).getChildNodes();


                    // Procura agora tags "atributo", e coloca num Map os pares de
                    // valores dos atributos do bloco
                    for (int j = 0; j < objNodeListAtributosBloco.getLength(); j++)
                        if (objNodeListAtributosBloco.item(j).getNodeName().equalsIgnoreCase("atributo"))
                            objAtributosBloco.put(objNodeListAtributosBloco.item(j).getAttributes().getNamedItem("nome").getNodeValue(), objNodeListAtributosBloco.item(j).getTextContent());

                    objBlocos[x][y] = CarregadorBlocos.getCarregadorBlocos().InstanciaBloco(strTipo, objVisao.ObterTamanhoBlocos());
                    objBlocos[x][y].CarregarAtributos(objAtributosBloco);
                    ConectaBloco(x, y);
                }
            }
        } catch (Exception e) {
            /* caso dê erro, recuperamos o backup salvo */
            objBlocos = objBackMatrizBlocos;
            boolArquivoSalvo = boolBackEstadoArquivo;
            throw e;
        }
        strNomeArquivo = strCaminhoArquivo;
    }
    public void SalvarArquivo() throws Exception
    {
        if (strNomeArquivo == null) throw new Exception("Nome do arquivo não definido previamente");
        SalvarArquivo(strNomeArquivo);        
    }
    public void SalvarArquivo(String strCaminhoArquivo) throws FileNotFoundException
    {
        String strChave;
        Map<String, String> objAtributos;
        
        PrintWriter out = new PrintWriter(new File(strCaminhoArquivo));
        out.println("<ConjuntoBlocos>");
        for (int x = 0; x < NUM_MAX_BLOCOS_LINHA; x++)
            for (int y = 0; y < NUM_MAX_BLOCOS_LINHA; y++)                
                if (objBlocos[x][y] instanceof Bloco)
                {
                    objAtributos = objBlocos[x][y].ObterAtributos();
                    
                    out.println("\t<Bloco tipo=\"" + objBlocos[x][y].getClass().getCanonicalName() + "\" x=\"" + String.valueOf(x) + "\" y=\"" + String.valueOf(y) + "\">");
                    
                    for (Iterator it = objAtributos.keySet().iterator(); it.hasNext();)
                    {
                        strChave = (String) it.next();
                        out.println("\t\t<atributo nome=\"" + strChave + "\">" + objAtributos.get(strChave) + "</atributo>");
                    }

                    out.println("\t</Bloco>");
                }
        out.println("</ConjuntoBlocos>");

        out.flush();
        out.close();
        strNomeArquivo = strCaminhoArquivo;
        boolArquivoSalvo = true;
    }

    public boolean getArquivoSalvo()
    {
        return boolArquivoSalvo;
    }
    public void NovoArquivo()
    {
        strNomeArquivo = null;
        objBlocos = new Bloco[NUM_MAX_BLOCOS_LINHA][NUM_MAX_BLOCOS_COLUNA];
        boolArquivoSalvo = true;
    }

    public void MostraBlocos(Graphics g)
    {
		if (objBlocos != null)
			for (int y = 0; y < NUM_MAX_BLOCOS_COLUNA; y++)
				for (int x = 0; x < NUM_MAX_BLOCOS_LINHA; x++)
					if (objBlocos[x][y] instanceof Bloco)
						objBlocos[x][y].Mostra(objVisao.ObterGraphics(g, x, y));
    }

    public void ExecutaVarredura()
    {
        if (objBlocos != null)
        {
            for (int y = 0; y < NUM_MAX_BLOCOS_COLUNA; y++)
                for (int x = 0; x < NUM_MAX_BLOCOS_LINHA; x++)
                    if (objBlocos[x][y] instanceof Bloco)
                        objBlocos[x][y].EventoTimer();

            for (int y = 0; y < NUM_MAX_BLOCOS_COLUNA; y++)
                for (int x = 0; x < NUM_MAX_BLOCOS_LINHA; x++)
                    if (objBlocos[x][y] instanceof Bloco)
                        objBlocos[x][y].Atualizar();
        }
    }
    public Bloco getBloco(int x, int y)
    {
        return objBlocos[x][y];
    }
    public void setBloco(int x, int y, Bloco b)
    {        
        objBlocos[x][y] = b;
        boolArquivoSalvo = false;
    }
    public void ConectaBloco(int x, int y)
    {
        Bloco b = objBlocos[x][y];
        if (b == null) return;

        if ((x > 0) && (objBlocos[x - 1][y] instanceof Bloco))
        {
            objBlocos[x - 1][y].ConectaBloco(Direcao.DIREITA, b);
            b.ConectaBloco(Direcao.ESQUERDA, objBlocos[x - 1][y]);
        }

        if ((y > 0) && (objBlocos[x][y - 1] instanceof Bloco))
        {
            objBlocos[x][y - 1].ConectaBloco(Direcao.BAIXO, b);
            b.ConectaBloco(Direcao.CIMA, objBlocos[x][y - 1]);
        }

        if ((x < NUM_MAX_BLOCOS_LINHA - 1) && (objBlocos[x + 1][y] instanceof Bloco))
        {
            objBlocos[x + 1][y].ConectaBloco(Direcao.ESQUERDA, b);
            b.ConectaBloco(Direcao.DIREITA, objBlocos[x + 1][y]);
        }

        if ((y < NUM_MAX_BLOCOS_COLUNA - 1) && (objBlocos[x][y + 1] instanceof Bloco))
        {
            objBlocos[x][y + 1].ConectaBloco(Direcao.CIMA, b);
            b.ConectaBloco(Direcao.BAIXO, objBlocos[x][y + 1]);
        }

        AtualizarBlocoEVizinhos(x, y);
        boolArquivoSalvo = false;
    }
    public void DesconectaBloco(int x, int y)
    {
        Bloco b = objBlocos[x][y];
        if (b instanceof Bloco)
        {
            b.RemoveBloco(Direcao.ESQUERDA);
            b.RemoveBloco(Direcao.CIMA);
            b.RemoveBloco(Direcao.DIREITA);
            b.RemoveBloco(Direcao.BAIXO);
        }

        if ((x > 0) && (objBlocos[x - 1][y] instanceof Bloco))
            objBlocos[x - 1][y].RemoveBloco(Direcao.DIREITA);

        if ((y > 0) && (objBlocos[x][y - 1] instanceof Bloco))
            objBlocos[x][y - 1].RemoveBloco(Direcao.BAIXO);

        if ((x < NUM_MAX_BLOCOS_LINHA - 1) && (objBlocos[x + 1][y] instanceof Bloco))
            objBlocos[x + 1][y].RemoveBloco(Direcao.ESQUERDA);

        if ((y < NUM_MAX_BLOCOS_COLUNA - 1) && (objBlocos[x][y + 1] instanceof Bloco))
            objBlocos[x][y + 1].RemoveBloco(Direcao.CIMA);


        AtualizarBlocoEVizinhos(x, y);
        boolArquivoSalvo = false;
    }

    private void AtualizarBlocoEVizinhos(int x, int y)
    {
        Bloco b = objBlocos[x][y];
        if (b instanceof Bloco)
            b.Atualizar();
        
        if ((x > 0) && (objBlocos[x - 1][y] instanceof Bloco)) objBlocos[x - 1][y].Atualizar();
        if ((y > 0) && (objBlocos[x][y - 1] instanceof Bloco)) objBlocos[x][y - 1].Atualizar();
        if ((x < NUM_MAX_BLOCOS_LINHA - 1) && (objBlocos[x + 1][y] instanceof Bloco)) objBlocos[x + 1][y].Atualizar();
        if ((y < NUM_MAX_BLOCOS_COLUNA - 1) && (objBlocos[x][y + 1] instanceof Bloco)) objBlocos[x][y + 1].Atualizar();
    }
}
