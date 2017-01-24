/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package blocos.model;

import java.io.*;
import java.util.*;

/**
 *
 * @author Leon
 */
public class CarregadorBlocos {

    private String strPathArquivos = "blocos_disponiveis/";
    private Map<String, Class> objMapNomesBlocos = new HashMap<String, Class>();
    private static CarregadorBlocos objInstancia = new CarregadorBlocos();
    private ClassLoaderInterno objClassLoaderInterno = new ClassLoaderInterno();
    private CarregadorBlocos()
    {
        CarregarBlocosDisponiveis();
    }
    public static CarregadorBlocos getCarregadorBlocos()
    {
        return objInstancia;
    }
    private class ClassLoaderInterno extends ClassLoader
    {
        public void CarregaClasse(String strNomeClasse) throws IOException, InstantiationException, IllegalAccessException
        {
            byte [] btConteudoClasse;
            if (!strNomeClasse.endsWith(".class")) strNomeClasse += ".class";
            btConteudoClasse = LerArquivo(strPathArquivos + strNomeClasse);
            Class objDescrClasse = defineClass(null, btConteudoClasse, 0, btConteudoClasse.length);

            for(int i = 0; i < objDescrClasse.getInterfaces().length; i++)
                if (objDescrClasse.getInterfaces()[i].equals(Bloco.class))
                    objMapNomesBlocos.put(objDescrClasse.getName(), objDescrClasse);
        }
    }

    public Bloco InstanciaBloco(String strTipoBloco, int Tamanho) throws InstantiationException, IllegalAccessException
    {
        if (!objMapNomesBlocos.containsKey(strTipoBloco)) throw new NoSuchElementException("Classe não encontrada nas classes disponiveis.");
        Bloco b = (Bloco)objMapNomesBlocos.get(strTipoBloco).newInstance();
        b.DefinirTamanho(Tamanho);
        return b;
    }
    private static byte [] LerArquivo(String strNomeArquivo) throws IOException
    {
        byte [] btConteudo;
        File f = new File(strNomeArquivo);
        InputStream is = new FileInputStream(f);
        
        btConteudo = new byte[(int)f.length()];
        
        int intOffset = 0;
        int intNumBytesLidos;
        while ((intOffset < btConteudo.length) &&
               (intNumBytesLidos = is.read(btConteudo, intOffset, btConteudo.length-intOffset)) >= 0)
        {
            intOffset += intNumBytesLidos;
        }
        is.close();
        return btConteudo;
    }
    public String[] ObterBlocosDisponiveis()
    {
        return (String [])objMapNomesBlocos.keySet().toArray(new String[0]);
    }
    
    public void CarregarBlocosDisponiveis()
    {
        File [] objArquivos;
        objArquivos = (new File(strPathArquivos)).listFiles();
        
        for (int i = 0; i < objArquivos.length; i++)
        {
            if (objArquivos[i].getName().endsWith(".class"))
            {
                try {
                    objClassLoaderInterno.CarregaClasse(objArquivos[i].getName());
                } catch (Exception e) {
                    // Arquivo que nao deu pra ser carregado, nada a se fazer...
                }
            }
        }
    }
}
