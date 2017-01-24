

public class Dicionario
{
	private static Dicionario d = null;
	private Dicionario()
	{
		// carrega os dados do arquivo aqui (por exemplo)
	}
	
	public boolean ExistePalavra(String strPalavra)
	{
		if (strPalavra.equalsIgnoreCase("the")) return true;
        if (strPalavra.equalsIgnoreCase("quick")) return true;
        if (strPalavra.equalsIgnoreCase("brown")) return true;
        if (strPalavra.equalsIgnoreCase("fox")) return true;
        if (strPalavra.equalsIgnoreCase("jumps")) return true;
        if (strPalavra.equalsIgnoreCase("over")) return true;
        if (strPalavra.equalsIgnoreCase("the")) return true;
        if (strPalavra.equalsIgnoreCase("lazy")) return true;
        if (strPalavra.equalsIgnoreCase("dog")) return true;


		return false;
	}
	
	public static Dicionario getDicionario()
	{
		if (d == null) d = new Dicionario();
		
		return d;		
	}
}