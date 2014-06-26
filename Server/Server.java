import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import de.shaoranlaos.dbAPI.DBAPI;
import de.shaoranlaos.dbAPI.Gruppe;
import de.shaoranlaos.dbAPI.Person;
import de.shaoranlaos.dbAPI.Tauschanfrage;
import de.shaoranlaos.dbAPI.Termin;


public class Server extends UnicastRemoteObject implements ServerInterface {
	DBAPI dbapi;
	public int ADMIN=3,GLEITER=2,APARTNER=1,MITGLIED=0;
	protected Server() throws RemoteException
	{
		super(55000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		dbapi = new DBAPI(); 
	}
	
	protected Server(String DBconf) throws RemoteException
	{
		super(55000, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
		dbapi = new DBAPI(DBconf); 
	}
	
	public static void main(String[] args)
	{
		try {
			Server server = new Server();
                        server.RMIinit();
	       
	
                //	int anz=server.anzeigenPersonen().length;
                //	Person[] personenarray = new Person[anz];
                //	personenarray=server.anzeigenPersonen();

                //	for(int i= 0;i<anz;i++)
                //	{
                //       	System.out.println("Nickname:"+personenarray[i].nickname);
                //         	System.out.println("Nachname:"+personenarray[i].nachname);
                //         	System.out.println("Vorname"+personenarray[i].vorname);
                //         	System.out.println("email:"+personenarray[i].email);

               //		}			

		} catch (RemoteException e) {
					
			e.printStackTrace();
		}
	}
	
	void RMIinit(){
		try {
			if (System.getSecurityManager() == null) {
    			System.setSecurityManager(new RMISecurityManager());
			}
			Registry RMIRegistry = null; 
			RMIRegistry = LocateRegistry.createRegistry(1099,new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null,null,true));
			System.out.println("RMI-Registry ausgefuehrt");
			RMIRegistry.rebind("Server", this);
			System.out.println("Server in die RMI-Registry eingetragen");
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
	}
	
    public int erstellenPerson(String Nutzername, String Passwort, String Email, String Status, String Nachname, String Vorname, String Geburtstag,int Gruppe )throws RemoteException 
    {
    	int ret;
        DBAPI dbapi = new DBAPI();
        String nickname = Nutzername;
        String passwort = Passwort ;
        String email = Email;
        String nachname = Nachname;
        String vorname = Vorname;
        String geburtstag = Geburtstag;
        int status = Integer.parseInt(Status);
        int gruppe = Gruppe;
        
        ret=dbapi.insertNewPerson( nickname,  passwort,  email,  status,  nachname,  vorname, geburtstag,gruppe);
        
        return  ret;
    }
     
    public Person anmeldenNutzer(String Nutzername, String Passwort)throws RemoteException
    {
        int PID;
    
        int anz = dbapi.getAllPersonen().length;
        Person[] tmpPersonen = new Person[anz];
        tmpPersonen = dbapi.getAllPersonen();
        
        if(dbapi.existPerson(Nutzername))
        {
            for(int i=0;i<anz;i++)
            {
                if(tmpPersonen[i].nickname.equals(Nutzername))
                {		
                    PID=tmpPersonen[i].id;
                    if( dbapi.cmpPasswort(PID, Passwort))
                    {	
			System.out.println(tmpPersonen[i].nickname + " angemeldet");

                        return (tmpPersonen[i]);
                    }
                    else
                        return null;
                }  
            }
        }
    	 return null;		 			
    }
	   
	public Person[] anzeigenPersonen()
	{
        int anz = dbapi.getAllPersonen().length;
        Person[] tmpPerson = new Person[anz];
        tmpPerson = dbapi.getAllPersonen();
        
        return tmpPerson;
	}
	
	public Person anzeigenPerson(int PersonenID)
	{	
		Person tmpPerson = new Person();
		tmpPerson=dbapi.getPerson(PersonenID);
		System.out.println("ja");
		return tmpPerson;
	}
	  
    //Gruppen ID wird vom angemeldeten Gruppenleiter automatisch vom Client �bergeben
    public boolean hinzufuegenMitglied(int PersonID,String GruppenID)throws RemoteException
    {
        if(dbapi.updatePerson(PersonID, "gruppe", GruppenID))
        {
          return true;
        }
        return false;
    }
	  
	public boolean loeschenMitglied(int PersonID)throws RemoteException
	{
	    
        if(dbapi.updatePerson(PersonID, "gruppe", "0"))
        {
          if(dbapi.updatePerson(PersonID, "rechte", String.valueOf(MITGLIED)))
          return true;
          else return false;
        }
        return false;
	}

    public boolean loeschenPerson(int PersonID)throws RemoteException
    {
        if(dbapi.deletePerson(PersonID))
        {
             return true;
        }
        return false;
    }
	  
    public boolean vergebenRechte(int PersonID, int rechte)throws RemoteException
    {
    
        if( dbapi.updatePerson(PersonID, "rechte", Integer.toString(rechte)))
            return true;
        else return false;
    }

    public Person[] anzeigenMitglieder(int GruppenID)throws RemoteException
    {
        int anz = dbapi.getAllPersonen().length;
        Person[] tmpPerson = new Person[anz];
        tmpPerson = dbapi.getAllPersonen();
        ArrayList<Person> arrli = new ArrayList<Person>();
        int counter=0;
        
        for(int i=0;i<anz;i++)
        {
            if(tmpPerson[i].gruppe == GruppenID)
            {
             arrli.add(tmpPerson[i]);
             counter++;
            }
        }
        Person[] newPerson = arrli.toArray(new Person[counter]);
        return newPerson;
    }
    
    //zeigt Nutzer an die noch keine Gruppe haben
    public Person[] anzeigenNichtMitglied()throws RemoteException
    {
        int anz = dbapi.getAllPersonen().length;
        Person[] tmpPerson = new Person[anz];
        tmpPerson = dbapi.getAllPersonen();
        ArrayList<Person> arrli = new ArrayList<Person>();
        int counter=0;
        
        for(int i=0;i<anz;i++)
        {
            if(tmpPerson[i].gruppe == 0)
            {
             arrli.add(tmpPerson[i]);
             counter++;
            }
        }
        Person[] newPerson = arrli.toArray(new Person[counter]);
        return newPerson;
    }
	  
    public  boolean loeschenGruppe(int GruppenID)throws RemoteException
    {
        int PID;
        
        int anz = dbapi.getAllPersonen().length;
        Person[] tmpPerson = new Person[anz];
        tmpPerson = dbapi.getAllPersonen();
        
        for(int i=0;i<anz;i++)
        {
            if(tmpPerson[i].gruppe==GruppenID)
            {
                PID=tmpPerson[i].id;
                if(!(dbapi.updatePerson(PID, "gruppe", "0")))
                    return false;
            }
        }
        
        if(dbapi.deleteGruppe(GruppenID)==true)
        {
            return true;
        }
        return false;
    }
	 
	public int ueberspringenTermin(int TerminID) throws RemoteException
	{
		int ret = dbapi.deleteTermin(TerminID);		
		return ret;
		
	}
	
	public boolean aussetzenTermin() throws RemoteException
	{
		if(dbapi.aussetzenTermin())
		{
			return true;
		}
		else return false;
	}
	
    public boolean aussetzenTermin(int TerminID)throws RemoteException
    {
        String DATE_FORMAT = "yyyy-MM-dd";
        Termin tmpTermin = new Termin();
        tmpTermin = dbapi.getTermin(TerminID);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String sDate = sdf.format(tmpTermin.datum);
        if((dbapi.aussetzenTermin(sDate)))
            return true;
        else return false;
    }
	
	public int erstellenTauschanfrage(int TerminIDQuelle, int TerminIDZiel)throws RemoteException
	{
		 if( dbapi.isGetauscht(TerminIDZiel))		//ueberprueft ob ZielTermin schon getauscht wurde wenn ja Abbruch
			 return -3;
		 else
		 {
    		 int ret = dbapi.insertTauschanfrage(TerminIDQuelle,TerminIDZiel);
    		 return ret;
		 }
	}
	
    // Termin ID �bergeben welcher Termin geandert werden soll, und Termin Objekt mit den geaenderten Daten
    public boolean aendernTermin(int TerminID, java.sql.Date Datum, java.sql.Time zeit, String ort, String essen) throws RemoteException
    {
        String DATE_FORMAT = "yyyy-MM-dd";
        String TIME_FORMAT = "hh:mm:ss";
        /*String gruppe;
        String ort;
        String essen;
        
        Termin tmpTermin = new Termin();
        tmpTermin = termin;*/
        
        
        //ort = tmpTermin.ort;
        if(!(dbapi.updateTermin(TerminID, "ort", ort)))
         return false;
        
        //essen = tmpTermin.essen;
        if(!(dbapi.updateTermin(TerminID, "essen", essen)))
         return false;
        
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String sDate = sdf.format(Datum);
        if(!(dbapi.updateTermin(TerminID, "datum", sDate)))
         return false;
        
        SimpleDateFormat sdf2 = new SimpleDateFormat(TIME_FORMAT);
        String sTime = sdf2.format(zeit);
        if(!(dbapi.updateTermin(TerminID, "zeit", sTime)))
         return false;
        
        return true;
     
    }
    
    public int einfuegenAuZykTermin(int GruppenID, Date Datum,Time zeit,String ort,String essen)throws RemoteException
    {
    	String DATE_FORMAT = "yyyy-MM-dd";
    	String TIME_FORMAT = "hh:mm:ss";
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String sDate = sdf.format(Datum);
        SimpleDateFormat sdf2 = new SimpleDateFormat(TIME_FORMAT);
        String sTime = sdf2.format(zeit);
        int ret = dbapi.hinzuAuZykTermin(GruppenID, sDate,sTime,ort,essen);
        return ret;
    }
    
	public Termin anzeigenTermin(int TerminID)throws RemoteException
	{	
        Termin tmpTermin = new Termin();
        tmpTermin=dbapi.getTermin(TerminID);
        
        return tmpTermin;
	}
	
	public Termin anzeigenTermin(Date Datum)
	{
		Termin[] tmpTermin= anzeigenTermine();
		
		for(int i=0;i<tmpTermin.length;i++)
		{
			if(tmpTermin[i].datum.compareTo(Datum)==0)
			{
				return tmpTermin[i];
			}
		}
		
		return null;
	}
	
	public Termin[] anzeigenTermine()
	{   
	    int anz = dbapi.getAllTermine().length;
	    Termin tmpTermin[] = new Termin[anz];
        tmpTermin = dbapi.getAllTermine();
        
        return tmpTermin;
	}
    
    public boolean loeschenAbHier(String Datum)throws RemoteException
	{
        //String DATE_FORMAT = "yyyy-MM-dd"; 
        
        //Termin tmpTermin = new Termin();
        //tmpTermin = dbapi.getTermin(TerminID);
        //SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        //String sDate = sdf.format(tmpTermin.datum);
        if( (dbapi.deleteTermine(Datum)))
         return true;
        else return false;
	}
	
	public boolean einfuegenAbHier()throws RemoteException
    {
        String sTime="13:00:00";
        Time defaultzeit = null;
        defaultzeit.valueOf(sTime);
         if( dbapi.insertZyklusTermine(defaultzeit))
        	 return true;
         else return false;
    }
	
    public boolean vergebenNick(String nickname)throws RemoteException
    {
        if(dbapi.existPerson(nickname)) //wenn existPerson true liefert existiert sie bereits und vergeben liefert auch true 
    	    return true;
        else return false;
    }
	 
    public boolean aendernPasswort(int PersonenID, String Passwort)throws RemoteException
    {
        if(dbapi.updatePerson(PersonenID, "passwort", Passwort))
         return true;
        else return false;
    }
    
    public boolean pruefenPasswort(int PersonenID, String Passwort)throws RemoteException
	{
        if(dbapi.cmpPasswort(PersonenID,Passwort))
        {
         return true;
        }
        return false;
	}
	
	public int erstellenGruppe(String Gruppenname, int GL_ID) throws RemoteException
    {
        int PID = GL_ID;
        int ret =dbapi.insertNewGruppe(Gruppenname, PID);
        
        if(ret==-1){
            //System.out.println("Gruppe Existiert schon!");
            return ret;
        }
        else if(ret==-2){
            //System.out.println("Es ist ein Fehler aufgetreten");
            return ret;
        } 
        else
            vergebenRechte(PID, GLEITER);
            dbapi.updatePerson(PID, "gruppe", String.valueOf(ret));
        return ret;
    }
    
    public boolean aendernGruppenleiter(int GruppenID, int PersonID)throws RemoteException
    {
        boolean ret  = dbapi.updatePerson(PersonID, "rechte", "GLEITER");
        //boolean ret2 = dbapi.updateGruppe(GruppenID, "leiter", "PersonID");
        if(ret == false )
        {
            return false;
        }
        else return true;
    }
    
    public boolean aendernGruppenname(int Gruppen_ID, String Gruppenname) throws RemoteException
    {
    	if(dbapi.updateGruppe(Gruppen_ID, "gruppenname", Gruppenname)==true)
    		return true;
    	else return false;
    }
    
    public Gruppe anzeigenGruppe(int GruppenID)throws RemoteException
    {	
        Gruppe tmpGruppe = new Gruppe();
        tmpGruppe=dbapi.getGruppe(GruppenID);
        
        return tmpGruppe;
    }
    
    public Gruppe[] anzeigenGruppen()throws RemoteException
    {
        int anz = dbapi.getAllGruppen().length;
        Gruppe[] tmpGruppe = new Gruppe[anz];
        tmpGruppe = dbapi.getAllGruppen();
        
        return tmpGruppe;
    }
	
    public boolean aendernNickname(int PersonenID, String Nickname)throws RemoteException
    {
        if(dbapi.updatePerson(PersonenID, "nickname", Nickname))
            return true;
        else return false;
    }
	 
    public boolean aendernVorname(int PersonenID, String Vorname)throws RemoteException
    {
        if(dbapi.updatePerson(PersonenID, "vorname", Vorname))
            return true;
        else return false;
    }
	 
    public boolean aendernNachname(int PersonenID, String Nachname)throws RemoteException
    {
        if(dbapi.updatePerson(PersonenID, "name", Nachname))
            return true;
        else return false;
    }
    
    public boolean aendernEmail(int PersonenID, String Email)throws RemoteException
    {
        if(dbapi.updatePerson(PersonenID, "e-mail", Email))
            return true;
        else return false;
    }
	 
    public boolean aendernGebDat(int PersonenID, String GebDat)throws RemoteException
    {
        if(dbapi.updatePerson(PersonenID, "geburtstag", GebDat))
            return true;
        else return false;
    }
	 
   /* public boolean aendernGruppe(int PersonenID, String GroupID)
    {
        if(dbapi.updatePerson(PersonenID, "gruppe", Group))
            return true;
        else return false;
    }*/
    
    //in CLient in einer Schleife
    public boolean aendernReihenfolge(int GID, String Reihenfolge)throws RemoteException
    {
        if(dbapi.updateGruppe(GID, "reihenfolge", Reihenfolge)==true)
        	return true;
        else return false;
        //gleich funktion aufrufen in der neue zyklusermine eingetragen werden?
    }
    
    public boolean existTauschanfrage(int TerminID)
    {
    	if(dbapi.existTauschanfrage(TerminID)==true)
    		return true;
    	else return false;
    }
    
    public int insertTauschanfrage(int TerminID_alt, int TerminID_neu)
    {
    	int ret=dbapi.insertTauschanfrage(TerminID_alt, TerminID_neu);
    		return ret;
    }
    
    public Tauschanfrage[] anzeigenTauschanfragenGruppe(int GruppenID)
    {
    	int anz = dbapi.getAllTauschanfragenToGruppe(GruppenID).length;
        Tauschanfrage[] tmpTA = new Tauschanfrage[anz];
        tmpTA = dbapi.getAllTauschanfragenToGruppe(GruppenID);
        
        return tmpTA;
    }
    
    public boolean executeTauschanfrage(int idTausch)
    {
    	if(dbapi.executeTauschanfrage(idTausch)==true)
    	{
    		return true;
    	}
    	else return false;
    }
    
    public boolean deleteTauschanfrage(int idTausch)
    {
    	int ret=0;
    	if(dbapi.deleteTauschanfrage(idTausch)==true)
    	{
    		return true;
    	}
    	else return false;
    }

  
    
}
