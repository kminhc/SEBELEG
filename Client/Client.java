import java.net.InetAddress;
import java.util.Arrays;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import de.shaoranlaos.dbAPI.Gruppe;
import de.shaoranlaos.dbAPI.Person;
import de.shaoranlaos.dbAPI.Tauschanfrage;
import de.shaoranlaos.dbAPI.Termin;
//Dies ist ein Kommentar

/*
* Dies ist your Blockkommentar
*	Return werte: 0 : Erfolg
*				  1 : Fehler von ServerFkt
*				  -9 : Nicht die erforderlichen Rechte
*/



public class Client implements ClientInterface{

//werte für die maximal zulässigen längen der eingaben
int laenge_nickname = 15;
int laenge_email = 40;
int laenge_name = 20;
int laenge_vname = 20;
int laenge_gruppenname = 15;
int laenge_ort = 15;
int laenge_essen = 50;
	
int rechte;	//0: MItglied; 1:Ansprechpartner; 2:Gruppenleiter; 3:Admin
Person AktNutzer = new Person();
//String host="localhost";
String host="shaoran.no-ip.org";
//String host="86.56.18.241";
//String host="animagic.servegame.com";
public ServerInterface server;
public int ADMIN=3,GLEITER=2,APARTNER=1,MITGLIED=0;
public Termin[] terminArray;
	public static void main(String[] args) {
		
		/*Client c= new Client();
		c.Init();
		try {
			c.erstellenPerson();
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
	}
	
	boolean Init()
	{
		return RMIinit();
		//konfiugurieren des Clients z.B. Config-Datei
	}
	
	boolean RMIinit(){

		String hostName = null;
		if (System.getSecurityManager() == null) {
    		System.setSecurityManager(new RMISecurityManager());
		}
		
        try {
			hostName = InetAddress.getByName(host).getHostAddress();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} 
		System.out.println(host);
		System.out.println(hostName);

		//System.setProperty("java.rmi.server.hostname", hostName);
		try {
		Registry registry = LocateRegistry.getRegistry(hostName, 10-9-9, new SslRMIClientSocketFactory());
		server = (ServerInterface)registry.lookup("Server");
		return true;
			//server = (Serverinterface) Naming.lookup("rmi://" + hostName + "/Server");
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} catch (NotBoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int anmeldenClient(String nutzername, String passwort) throws RemoteException
	{
		/*Scanner sc = new Scanner(System.in);
		System.out.println("Bitte geben Sie ihren Nutzername ein. Achten Sie auf Gro?- und Kleinschreibung");
		nutzername = sc.next();
		System.out.println("Bitte geben Sie ihr Passwort ein. Achten Sie auf Gro?- und Kleinschreibung");
		passwort = sc.next();
		System.out.println("Bitte haben Sie Geduld. Die Anmeldung wird durchgef?hrt");*/
		
		try {
		AktNutzer = server.anmeldenNutzer(nutzername, passwort);
		
			if( AktNutzer != null)	//Anmeldung erfolgreich?
			{
				rechte = AktNutzer.rechte;	//?ber GUI regeln welche Funktionen "sichtbar" sind
				return 0;
			}
			else return 1;
		} catch (RemoteException e1) {
			return 2;
		}
	}
	
	public int erstellenPerson(String nutzername, String passwort, String passwortWdh, String vorname, String nachname, String geburtstag,String email) throws RemoteException
	{
	    if(AktNutzer.rechte==ADMIN)
	    {
	    	if (nutzername.isEmpty()) return 1;
	    	if (passwort.isEmpty()) return 2;
	    	if (passwortWdh.isEmpty()) return 3;
	    	if (!passwort.equals(passwortWdh)) {
	    		return 6;
	    	}
	    	if (vorname.isEmpty()) return 4;
	    	if (nachname.isEmpty()) return 5;

    		//String nachname,  vorname,  geburtstag,  nutzername,  status, passwort;
    		//Scanner sc = new Scanner(System.in);
    		boolean vergeben=false;
    		do
    		{
    			//System.out.println("Bitte geben Sie den Nutzernamen/Nicknamen ein");
    			//nutzername = sc.next();
    			vergeben=server.vergebenNick(nutzername);
    			if(vergeben == true) {
    				//System.out.println("Bitte nochmal probieren");
    				return 7;
    			}
    		}while(vergeben==true);
    		/*System.out.println("Bitte geben Sie den Vorname ein");
    		vorname = sc.next();
    		System.out.println("Bitte geben Sie den Nachnamen ein");
    		nachname = sc.next();
    		
    		System.out.println("Bitte geben Sie den Geburtstag ein. Das Format: [YYYY-MM-DD] ist einzuhalten");*/
    		//geburtstag = sc.next();
    		String status = Integer.toString(MITGLIED);	//Default
    		//passwort = "1234";                  	//Default
    		int gruppe = 0;                        //ditto
    		//String email = "email4spast@gmail.com"; //ditto
    		if( (server.erstellenPerson( nutzername,  passwort,  email,  status,  nachname,  vorname,  geburtstag,  gruppe))>0)	{		//rechte=status? int oder String?
    			//System.out.println("Nutzer wurde erfolgreich erstellt");
    			return 0;
    		}
    		else {
    			return 8;
    			//System.out.println("Es gab einen Fehler beim Erstellen des Nutzers");
    		}
	    }
	    
	    return -1;
	}
	
	public int loeschenNutzer(int PID) throws RemoteException
	{
		if(AktNutzer.rechte==ADMIN)
		{
			Person tmpPerson = server.anzeigenPerson(PID);
			if(tmpPerson.rechte == GLEITER)
			{
				return 2;//ausgewählte Person ist Gruppenleiter-->erst Gruppenleiter der Gruppe ändern
			}
			if(server.loeschenPerson(PID)==true)
			return 0;
			else return 1;
		}else return -9;
	}
    		/*Scanner sc = new Scanner(System.in);
    		//Alle Personen anzeigen um ID zu bekommen
    		int anz= server.anzeigenPersonen().length;
    		Person[] tmpPersonen = new Person[anz];
    		tmpPersonen = server.anzeigenPersonen();
    		for(int i=0;i<anz;i++)
    		{			
    			System.out.println("Id: " + tmpPersonen[i].id);
    			System.out.println("Nutzername: " + tmpPersonen[i].nickname);
    			System.out.println("Nachname: " + tmpPersonen[i].nachname); 
    			System.out.println("Vorname: " + tmpPersonen[i].vorname);	
    		}
    		//ID auswaehlen zum loeschen
    		 System.out.println("Bitte geben Sie an welchen Nutzer sie loeschen wollen. Geben Sie die ID an");
    		 int pId = Integer.parseInt(sc.next());
    		 Person tmpPerson = new Person();
    		 tmpPerson = server.anzeigenPerson(pId);
    		 if(tmpPerson.rechte == GLEITER)
    		 {
    			 return 2;
    		     //System.out.println("Dieser Nutzer ist ein Gruppenleiter! der Gruppe" +tmpPerson.gruppe);
    		 }
    		 if(tmpPerson != null)
    		 {
        		 System.out.println("Nutzername: " + tmpPerson.nickname);
        		 System.out.println("Nachname: " + tmpPerson.nachname);
        		 System.out.println("Vorname: " + tmpPerson.vorname);
        		 System.out.println("");
        		
        		 boolean abbrechen = weiterAendern();
        		 if( abbrechen==false)
        		 {
        			 if( server.loeschenPerson(pId) )
        				 return 0;
        			 //System.out.println("Loeschen erfolgreich");
        			 else return 1;
        			 //System.out.println("Loeschen fehfgeschlagen");
        		 }
        		
        		 else return 3;
        				 //System.out.println("Loeschen wurde abgebrochen");	//Keine gueltige Antwort
    		 }
    		 else return 4;
    				 //System.out.println("Keine gueltige pId.");
		}
		else
		   return -9;// System.out.println("Sie haben keine Berechtigung Nutzer zu loeschen. Wenden Sie sich an den Admin.");
	}*/
	
	public int aendernNickname(String nutzername) throws RemoteException
	{
		boolean vergeben=false;
		//String nutzername;
	    //Scanner sc = new Scanner(System.in);
	    //kann jeder machen der angemeldet ist, also keine Rechteabfrage
	    //System.out.println("Bitte geben Sie den neuen Nickname ein.");
	    //String nickname_new = sc.next();
	   //do
		//{
			//System.out.println("Bitte geben Sie den Nutzernamen/Nicknamen ein");
			//nutzername = sc.next();
		int ret = pruefenLaenge(nutzername,laenge_nickname);
		if(ret == 1)
			return 3;
		vergeben=server.vergebenNick(nutzername);
		if(vergeben == true) return 1;//{System.out.println("Bitte nochmal probieren");}
		//}while(vergeben==true);
		if( server.aendernNickname(AktNutzer.id,nutzername))
		{
		    return 0;
		}
		else return 2;
	}
	
	
	public int aendernVorname(String vorname_new) throws RemoteException
	{
	    //Scanner sc = new Scanner(System.in);
	    //kann jeder machen der angemeldet ist, also keine Rechteabfrage
	    //System.out.println("Bitte geben Sie den neuen Nickname ein.");
	    //String vorname_new = sc.next();
		
		int ret = pruefenLaenge(vorname_new,laenge_vname);
		if(ret == 1)
			return 2;

	    if( server.aendernVorname(AktNutzer.id,vorname_new))
		{
		   // System.out.println("Eerfolgreich geaendert");
	    	return 0;
		}
	    
	    else return 1;//System.out.println("Fehler beim Aendern" );
	}
	
	public int aendernNachname(String nachname_new) throws RemoteException
	{
	    //Scanner sc = new Scanner(System.in);
	    //kann jeder machen der angemeldet ist, also keine Rechteabfrage
	    //System.out.println("Bitte geben Sie den neuen Nachnamen ein.");
	    //String nachname_new = sc.next();
		
		int ret = pruefenLaenge(nachname_new,laenge_name);
		if(ret == 1)
			return 2;
		
	    if( server.aendernNachname(AktNutzer.id,nachname_new))
		{
	    	return 0;
		}
	    else return 1;
	}
	
	public int aendernEmail(String email_new) throws RemoteException
	{
	    //Scanner sc = new Scanner(System.in);
	    //kann jeder machen der angemeldet ist, also keine Rechteabfrage
	    //System.out.println("Bitte geben Sie den neuen Nickname ein.");
	    //String email_new = sc.next();
	    
	    int ret = pruefenLaenge(email_new,laenge_email);
		if(ret == 1)
			return 2;
	    
	    if( server.aendernEmail(AktNutzer.id,email_new))
		{
		    return 0;
	    	//System.out.println("Erfolgreich geaendert");
		}
	    
	    else 
	    	return 1;//System.out.println("Fehler beim Aendern" );
	}

    public int aendernGebDat(String datum) throws RemoteException
	{
	    //Scanner sc = new Scanner(System.in);
	    //kann jeder machen der angemeldet ist, also keine Rechteabfrage
	    //System.out.println("Bitte geben Sie den neuen Geburtsdatum ein Bsp:1-900-11-26.");
	    //String gebdat_new = sc.next();
	    
	    if( server.aendernGebDat(AktNutzer.id,datum))
		{
		    return 0;//System.out.println("Erfolgreich geaendert");
		}
	    
	    else return 1;//System.out.println("Fehler beim Aendern" );
	}
	
	/*public void aendernGruppe()
	{
	    //OFFENE FRAGEN: -Gruppenleiter kann die Rechte der Mitglieder innerhalb seiner Gruppe Ã¤ndern, ... AAAAAAAAAAAAADMIN? /SPAST!was solln das? sow was von unprofessionell
	    //               -Gruppenleiter kann nur Gruppe Wechseln Ã¼ber Admin, der zuerst neun(-9) Gruppenleiter ernennt und dann Fribeier fÃ¼r niemanden

	              
	    //kann nur Gruppenleiter
        if(AktNutzer.rechte==GLEITER)
		{
			//System.out.println("Bitte geben Sie den Gruppennamen an");
			//String gName = sc.next();
			int anz= server.anzeigenPersonen().length;
			if (anz > 0)
			{
				Person[] tmpPersonen = new Person[anz];
				int anz2=0;
				for(int i=0;i<anz;i++)
				{			
					if( tmpPersonen[i].gruppe == AktNutzer.gruppe)
					{
						System.out.println("Id: " + tmpPersonen[i].id);
						System.out.println("Nutzername: " + tmpPersonen[i].nickname);
						System.out.println("Nachname: " + tmpPersonen[i].nachname);
						System.out.println("Vorname: " + tmpPersonen[i].vorname);
						System.out.println("Gruppe: " + tmpPersonen[i].gruppe);
						anz2++;
					}
				}
				if(anz2>0){
					boolean gefunden=false;
					
					System.out.println("bitte geben sie die Id der Person an dessen Gruppenzugehoerigkeit Sie aendern wollen.");
					int tmpId = Integer.parseInt(sc.next());
					for (int i = 0; i < tmpPersonen.length; i++) {
						if((tmpId==tmpPersonen[i].id)&&(tmpPersonen[i].gruppe==AktNutzer.gruppe)){
							gefunden=true;
							break;
						}
					}
					if(gefunden){
						if( server.erstellenGruppe(gName, String.valueOf(GL_ID)))
						{
							System.out.println("Grueppchen erfolgeich ersteelt");
						}
						else System.out.println("Grueppchen nicht erfolgeich erstellt");
					}
					else System.out.println("Diese Person kann nicht als Gruppenleiter eingesetzt werden");
				}
				else System.out.println("Bitte erstellen Sie erst eine neue Person");
			}
			else System.out.println("Erst Person anlegen");
		}
	}
	*/    
	
   public int vergebenRechte(int PID,int Rechte) throws RemoteException
    {
    	if(AktNutzer.rechte >= GLEITER)
    	{
    		if(server.vergebenRechte(PID, Rechte))
    		{
    			return 0;
    		}else return 1;
    	}
		return -9;
    }
    	
    
	public int loeschenMitglied(int PID) throws RemoteException
	{
	   //Mitglied aus der Gruppe entfernen
	    if(AktNutzer.rechte == GLEITER)
	    {
	           //Gruppenmitglieder sind angezeigt
	    	//PID wird über GUI angeklickt
	    	if(server.loeschenMitglied(PID))
	    		return 0;
	    	else return 1;
        }
	    else return -9;
	}
	
	//alte variante loeschenGlied
	//Scanner sc = new Scanner(System.in);
    /*int anz = server.anzeigenPersonen().length;
    if (anz > 0)    //es gibt Ã¼berhaupt mitglieder
	{
			Person[] tmpPersonen = new Person[anz];
			tmpPersonen = server.anzeigenPersonen();
			int anz2=0;
			for(int i=0;i<anz;i++)
			{			
				if( tmpPersonen[i].gruppe == AktNutzer.gruppe)
				{
					System.out.println("Id: " + tmpPersonen[i].id);
					System.out.println("Nutzername: " + tmpPersonen[i].nickname);
					System.out.println("Nachname: " + tmpPersonen[i].nachname);
					System.out.println("Vorname: " + tmpPersonen[i].vorname);
					anz2++;
				}
			}
			if(anz2>0)  //Zeigt alle Mitglieder der Gruppenleiter Gruppe
			{
				boolean gefunden=false;
				System.out.println("Geben Sie die Id an, welche Person die Gruppe verlassen soll");
				int PID = Integer.parseInt(sc.next());
				for (int i = 0; i < tmpPersonen.length; i++) {
					if((PID==tmpPersonen[i].id)&&(tmpPersonen[i].gruppe==AktNutzer.gruppe) &&(PID != AktNutzer.id)){
						gefunden=true;
						break;
					}
			}
			if(gefunden)
			{
			    if( server.loeschenMitglied(PID))
			    {
			       return 0;
			       //System.out.println("Erfolgreich aus Gruppe entfernt");
			    }
			    else return 1;
			    //fehler beim loeschen
			}
			else return 2;
			//System.out.println("Diese Person kann nicht aus der Gruppe entfernt werden");
    }
    else return 3;
			//System.out.println("Es gibt keine die aus der Gruppe entfernt werden kann");
}
else return 4;
    //System.out.println("Keine Personen vorhanden");*/
	
	public int hinzufuegenMitglied(int PID) throws RemoteException
	{
	    //Scanner sc = new Scanner(System.in);
	    if(AktNutzer.rechte == GLEITER)
	    {
	    	//String GID = Integer.toString(AktNutzer.gruppe);
	    	if(server.hinzufuegenMitglied(PID, String.valueOf(AktNutzer.gruppe)))
	    	{
	    		return 0;
	    	}
	    	else return 1;
	    } return -9;
	}	
	
	//alte version hinzufügenMIt
    	       /* int anz = server.anzeigenPersonen().length;
    	        if (anz > 0)    //es gibt ï¿½berhaupt mitglieder
    			{
        				Person[] tmpPersonen = new Person[anz];
        				int anz2=0;
        				for(int i=0;i<anz;i++)
        				{			
        					if( tmpPersonen[i].gruppe == -1)
        					{
        						System.out.println("Id: " + tmpPersonen[i].id);
        						System.out.println("Nutzername: " + tmpPersonen[i].nickname);
        						System.out.println("Nachname: " + tmpPersonen[i].nachname);
        						System.out.println("Vorname: " + tmpPersonen[i].vorname);
        						anz2++;
        					}
        					
        				}
        				if(anz2>0)  //es gibt Mitglieder die noch keine Gruppen haben
        				{
            				boolean gefunden=false;
            				System.out.println("Geben Sie an welche Person hinzugefuegt werden soll");
            				int PID = Integer.parseInt(sc.next());
            				for (int i = 0; i < tmpPersonen.length; i++)
            				{
        						if((PID==tmpPersonen[i].id)&&(tmpPersonen[i].gruppe==-1))
        						{
        							gefunden=true;
        							break;
        						}
        				    }
        				if(gefunden)
        				{
        				    if( server.hinzufuegenMitglied(PID,String.valueOf(AktNutzer.gruppe)))
        				    {
        				    	return 0;
        				        //System.out.println("Nutzer" + PID +"erfolgreich hinzugefuegt");
        				    }
        				    else return 1;
        				    //Fehler beim hinzuf?gen
        				}
        				else return 2;
        				//System.out.println("Diese Person kann nicht als Mitglied eingesetzt werden");
        	    }
        	    else return 3;
        				//System.out.println("Keine Person ohne Gruppe");
    	    }
    	    else return 4;
    	        //System.out.println("Keine Personen vorhanden");
        }
	    else return -9;*/
	   
	
	
	public Person[] anzeigenPersonen() throws RemoteException{
		return server.anzeigenPersonen();
	}
		
	public int erstellenGruppe(String gName, String GL_ID) throws RemoteException
	{
		
		//Scanner sc = new Scanner(System.in);
		if(AktNutzer.rechte==ADMIN)
		{
			int ret = pruefenLaenge(gName,laenge_gruppenname);
			if(ret == 1)
				return -3;
			//System.out.println("Bitte geben Sie den Gruppennamen an");
			//String gName = sc.next();
			/*int anz= server.anzeigenPersonen().length;
			if (anz > 0)
			{
				Person[] tmpPersonen = new Person[anz];
				tmpPersonen = server.anzeigenPersonen();
				int anz2=0;
				for(int i=0;i<anz;i++)
				{			
					if( tmpPersonen[i].gruppe == 0)
					{
						System.out.println("Id: " + tmpPersonen[i].id);
						System.out.println("Nutzername: " + tmpPersonen[i].nickname);
						System.out.println("Nachname: " + tmpPersonen[i].nachname);
						System.out.println("Vorname: " + tmpPersonen[i].vorname);
						anz2++;
					}
				}
				if(anz2>0){
					boolean gefunden=false;*/
			//anzeigenNichtMitglied();
			
			int ret2 = server.erstellenGruppe(gName, Integer.parseInt(GL_ID));
			return ret2;		//ret==-1 Gruppenname gibts schon; ret==-2 Fehler und >0 also die GruppenID supi hat geklappt
			
			//System.out.println("bitte geben sie die Id des Gruppenleiters an");
			//int GL_ID = Integer.parseInt(sc.next());
					/*for (int i = 0; i < tmpPersonen.length; i++) {
						if((GL_ID==tmpPersonen[i].id)&&(tmpPersonen[i].gruppe==0)){
							gefunden=true;
							break;
						}
					}
					if(gefunden){
						int ret = server.erstellenGruppe(gName, String.valueOf(GL_ID));
						//if(ret>=0)
						//{
						//	return 0;
							//System.out.println("Grueppchen erfolgeich ersteelt");
						//}
						//else return 1;
						//System.out.println("Fehler beim erstellen");
						return ret;
					}
					else return 1;
					//System.out.println("Diese Person kann nicht als Gruppenleiter eingesetzt werden");
				}
				else return 2;
				//System.out.println("Bitte erstellen Sie erst eine neue Person");
			}
			else return 3;
			//System.out.println("Erst Person anlegen");*/
		}
		else return -9;
	}
	
	public int aendernGruppenname(String gName_new) throws RemoteException
	{
		if(AktNutzer.rechte == GLEITER)
		{
			int ret = pruefenLaenge(gName_new,laenge_gruppenname);
			if(ret == 1)
				return 2;
			if(server.aendernGruppenname(AktNutzer.gruppe, gName_new)==true)
				return 0;
			return 1;
		}else return -9;
	}
	
	public int aendernGruppenleiter(int GruppenID, int PID) throws RemoteException
	{
		if(AktNutzer.rechte == ADMIN)
		{
			Gruppe tmpGruppe = server.anzeigenGruppe(GruppenID);
			if(server.vergebenRechte(tmpGruppe.leiter, MITGLIED)==false)
				return 1;//Fehler beim Ändern des alten GL
			if(server.aendernGruppenleiter(GruppenID, PID))
				return 0;
			else return 2;
   	 	}else return -9;
	}
	     
    	    //Alle Gruppen anzeigen um ID auzwaehlen aus welcher Gruppe der GLEITER ge?ndert werden soll
    		/*int Gruppenanzahl= server.anzeigenGruppen().length;
    		Gruppe[] tmpGruppen = new Gruppe[Gruppenanzahl];
    		if(Gruppenanzahl>0)
    		{
    		    for(int i=0;i<Gruppenanzahl;i++)
        		{			
        			System.out.println("Id: " + tmpGruppen[i].id);
        			System.out.println("Name: " + tmpGruppen[i].name);
        			System.out.println("Leiter: " + tmpGruppen[i].leiter);
        		} 
        	     
        	    Scanner sc = new Scanner(System.in);
        	    System.out.println("Id angeben, welche Gruppe einen neuen GL haben soll");
        	    int GruppenID = Integer.parseInt(sc.next());
        	    Gruppe tmpGruppe = server.anzeigenGruppe(GruppenID);
        	    //nur wenn gueltige ID dann Infos dazu ausgeben
        	    if(tmpGruppe != null)
        	    {
        	        System.out.println("Gruppen_ID: "+tmpGruppe.id);
        	        System.out.println("GruppenName: "+tmpGruppe.name);
        	        System.out.println("GruppenLeiter: "+tmpGruppe.leiter);
        	        System.out.println("");
        	        System.out.println("Aktuelle Mitglieder");
            	    int Personenanzahl= server.anzeigenPersonen().length;
            		if (Personenanzahl > 0)
            		{
            			Person[] tmpPersonen = new Person[Personenanzahl];
            			int anz=0;
            			for(int i=0;i<Personenanzahl;i++)
            			{	//Anzeigen aller Gruppenmitglieder ausser dem GLEITER		
            				if( tmpPersonen[i].gruppe == tmpGruppe.id && tmpPersonen[i].id != tmpGruppe.leiter)
            				{
            					System.out.println("Id: " + tmpPersonen[i].id);
            					System.out.println("Nutzername: " + tmpPersonen[i].nickname);
            					System.out.println("Nachname: " + tmpPersonen[i].nachname);
            					System.out.println("Vorname: " + tmpPersonen[i].vorname);
            					anz++;
            				}
            			}
            			if(anz>0)  //es gibt Mitglieder der Gruppe die nicht GLEITER sind
        				{
            				boolean gefunden=false;
            				System.out.println("Geben Sie Id der Person an, die neuer Gruppenleiter werden soll.");
            				int GL_new = Integer.parseInt(sc.next());
            				
            				for (int i = 0; i < tmpPersonen.length; i++) 
            				{
        						if((GL_new==tmpPersonen[i].id)&& ( tmpGruppe.id==tmpPersonen[i].gruppe) && (tmpPersonen[i].rechte != GLEITER))
        						{
        							gefunden=true;
        							break;
        						}
        				    }
            				if(gefunden)
            				{
            				    //KRITISCHER ABSCHNITT, wenn erste Bedingung erfuellt und zweite nicht, dann hat Gruppe kein Gruppenleiter!!!!!!!
            				    if( (server.vergebenRechte(tmpGruppe.leiter,MITGLIED)) && (server.aendernGruppenleiter(GruppenID, GL_new)) )
            			        {
            				    	return 0;
            			           // System.out.println("Gruppenleiteraenderung erfolgreich");
            			        }
            			        else return 1;
            				    //System.out.println("Es ist ein Fehler aufgetreten");
            				}
            			    else return 2;
            				//System.out.println("Ungueltige PID eingegeben");
        				}
        				else return 3;
            			//System.out.println("Kein Mitglied vorhanden, das Gruppenleiter werden kann");
        	    		
                    }
                    else  return 4;
            		//System.out.println("Gruppe enthaelt keine Mitglieder.");
        	    
        		}
        		else  return 5;
        	    //System.out.println("Kein Gruppe mit dieser Id vorhanden"); //kann eigentlich nicht vorkommen oder?
    		}
    		else return 6;
    		//System.out.println("Es existiert noch keine Gruppe.");
	    }
	     else return -9;
	}*/
	
	public int loeschenGruppe(int GruppenID) throws RemoteException
	{
	    if(AktNutzer.rechte==ADMIN)
	    {
	    	if(server.loeschenGruppe(GruppenID))
	    		return 0;
	    	else return 1;
	    }return -9;
    		/*Scanner sc = new Scanner(System.in);
    		//Alle Gruppen anzeigen um GruppenId zu bekommen
    		int anz= server.anzeigenGruppen().length;
    		Gruppe[] tmpGruppen = new Gruppe[anz];
    		for(int i=0;i<anz;i++)
    		{			
    			System.out.println("Id: " + tmpGruppen[i].id);
    			System.out.println("Gruppenname: " + tmpGruppen[i].name);
    		}
    		//
    		System.out.println("Bitte geben Sie an welchen Gruppe sie loeschen wollen. Geben Sie die ID an");
    		 int gId = Integer.parseInt(sc.next());
    		 Gruppe tmpGruppe = new Gruppe();
    		 tmpGruppe = server.anzeigenGruppe(gId);
    		 System.out.println("GruppenID: " + tmpGruppe.id);
    		 System.out.println("Name: " + tmpGruppe.name);
    		 boolean abbrechen = weiterAendern();
    		 
    		 if( abbrechen==false)
    		 {
    			 if( (server.loeschenGruppe(gId)))
    				 return 0;
    				 //System.out.println("Loeschen erfolgreich");
    			 else return 1;
    			 //System.out.println("Fehler beim lï¿½schen");
    		 }
    	
    		 else 
    			 return 2;//System.out.println("Loeschen abgebrochen?");	//Keine g?ltige Antwort*/
	    
	}
	/*
	public void wertAendern()
	{
		System.out.println("Bitte geben Sie etwas ein");
		 int gId = Integer.parseInt(sc.next());
		
		
	}*/
	
	
	
	
	public int aendernTermin(int TerminID, java.sql.Date Datum, java.sql.Time zeit, String ort, String essen) throws RemoteException
	{
	   if(AktNutzer.rechte >APARTNER)
	   {
		   int ret = pruefenLaenge(ort,laenge_ort);
			if(ret == 1)
				return 2;
			int ret2 = pruefenLaenge(essen,laenge_essen);
			if(ret2 == 1)
				return 3;
			
			java.util.Date date = new java.util.Date();			//gibt das aktuelle Datum
    		//getMonth()== ... getYear()== ... getDay() - currentDay >=3
    	    java.sql.Date sqlDate = new java.sql.Date(date.getDate());	//wandelt aktu Datum von util.Date in sql.Date um
    	    int dayDiff = Datum.getDate() - sqlDate.getDate();
    	    int monthDiff =Datum.getMonth() -sqlDate.getMonth();
    	    int yearDiff = Datum.getYear() -sqlDate.getYear();
    	    
    	    int anz = server.anzeigenTermine().length;
		    Termin tmpTermin[] = new Termin[anz];
	        tmpTermin = server.anzeigenTermine();
	        for(int i=0;i<anz;i++)
	        {
	        	if(tmpTermin[i].datum == Datum)
	        		return 4;//bereits ein termin an diesem Datum
	        }
    	    
    	    if(dayDiff >=3 && monthDiff >=0 && yearDiff>=0)
			{
				if(server.aendernTermin(TerminID, Datum,zeit,ort,essen))
					return 0;
				else return 1;
			}else return 5;//keine 3 Tage hin bis zu diesen Termin
    	    
	   }
	   return -9;
	}
    		//Scanner sc = new Scanner(System.in);
    		
    		//System.out.println("Welchen Termin moechten SIe aendern?");
    		//int TerminID = Integer.parseInt(sc.next());
    		
    		/*String TIME_FORMAT = "hh:mm:ss";
    		do
    		{
    			System.out.println("Was moechten Sie aendern?");
    			System.out.println("Druecken Sie 1 fuer das Datum");
    			System.out.println("Druecken Sie 2 fuer den Ort");
    			System.out.println("Druecken Sie 3 fuer die Zeit");
    			System.out.println("Druecken Sie 4 fuer das Essen");
    			System.out.println("Oder druecken Sie -9 zum beenden");
    			int wert = Integer.parseInt(sc.next());
    			switch(wert)
    			{
    			case 1: System.out.println("Geben SIe das (geaenderte) Datum ein");
    					Date d = null; // new Date();
    					termin.datum = d.valueOf(sc.next());
    			 		fertig=weiterAendern();
    					 break;
    			case 2: System.out.println("Geben Sie den (geaenderte) Ort ein");
    		        	termin.ort = sc.next();
    		        	fertig=weiterAendern();
    					 break;
    			case 3: System.out.println("Geben Sie den (geaenderte) Zeitwunsch ein");
    					String tmpZeit = sc.next();
    					SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
    					termin.zeit = java.sql.Time.valueOf(tmpZeit);
    		        	fertig=weiterAendern();
    		        	break;
    			case 4: System.out.println("Geben Sie das (geaenderte) Essen ein");
    					termin.essen = sc.next();
    					fertig=weiterAendern();
    					 break;
    			case -9: fertig = true;
    					break;
    			default: System.out.println("Falsche angabe");
    			}
    		}while(fertig==false);
    		*/
    		
	   
	
	public int aendernPasswort(String pw_new) throws RemoteException
	{
		if(server.aendernPasswort(AktNutzer.id, pw_new))
			return 0;
		else return 1;
	}
		/*String pw_old;
		Scanner sc = new Scanner(System.in);
		System.out.println("Bitte geben Sie ihr altes Passwort ein.");
		pw_old = sc.next();
		if( server.pruefenPasswort(AktNutzer.id,pw_old)){
			System.out.println("Bitte geben Sie ihr neues PW ein");
			String pw_new = sc.next();
			System.out.println("Bitte bestaetigen Sie ihr neues PW");
			String test = sc.next();
			if( test == pw_new)
			{
				if (server.aendernPasswort(AktNutzer.id, pw_new))
				{
					AktNutzer.passwort = pw_new;
					return 0;
					//System.out.println("PW erfolgreich geaendert");	
				} else return 1;
				//System.out.println("Fehler beim Aendern");
			}
			else return 2;
			//System.out.println("Werte stimmen nicht ueberein. Versuchen Sie es erneut");
		}
		else return 3;
		//System.out.println("Falsches PW");
	}*/
	
	
	
	public void ladeTermine()throws RemoteException {
		int anz=server.anzeigenTermine().length;
		terminArray = new Termin[anz];
		terminArray = server.anzeigenTermine();
	}
	
	public Termin anzeigenTermin(Date datum)throws RemoteException
	{
		for (int i=0; i < terminArray.length; i++)
		{
			if (terminArray[i].datum.compareTo(datum) == 0) {
				return terminArray[i];
			}
		}
		
		return null;
	}
	
	public Termin[] anzeigenTermine() throws RemoteException
	{
		return terminArray;
	}
	
	public Termin[] anzeigenTauschtermine(int id) throws RemoteException
	{
		ArrayList<Termin> termine = new ArrayList<Termin>();
		
		Termin[] tmpTermine = terminArray;
		int anz = tmpTermine.length;
		int count = 0;
		for (int i=0; i<anz; i++) {
			if (tmpTermine[i].id != id && tmpTermine[i].gruppe != AktNutzer.gruppe) {
				termine.add(tmpTermine[i]);
				count++;
			}
		}
		
		return termine.toArray(new Termin[count]);
	}
	
	public int aendernReihenfolge(String GIDString) throws RemoteException
	{//kriegt ein array von GruppenID's
	    //variable boolean reihenfolge_geaendert zum mitteilen dass sich die reihenfolge geaendert hat zum eintragen der neuen zyklustermine?
	    if(AktNutzer.rechte == ADMIN)
	    {
	    	//Idee: vgl des von gui bekommenen sortierteb Gruppenarrays mit dem sort DB array
	    	// wenn beide gleich dann wurde kein gruppe vergesse und es gibt keine doppelt
	    	// dann bekommenes array auslesen und von 1 beginnend reihenfolge attribut setzen 

	    	//erst alle Gruppen mit reihenfolge anzeigen
    	    /*int anz= server.anzeigenGruppen().length;
        	Gruppe[] tmpGruppen = new Gruppe[anz];
	    	*/
	    	GIDString = GIDString.replaceAll(" ","");
	    	String[] GString = GIDString.split(",");
	    	int[] reihenfolge_GID = new int[GString.length];
	    	for(int x=0;x<GString.length;x++)
	    	{
	    		reihenfolge_GID[x]=Integer.parseInt(GString[x]);
	    	}
	    	int[] copy = new int[reihenfolge_GID.length];
	    	copy = reihenfolge_GID.clone();
	    	Arrays.sort(copy); 
	    	int anz = server.anzeigenGruppen().length;
	    	Gruppe[] tmpGruppen = new Gruppe[anz];
	    	tmpGruppen = server.anzeigenGruppen();
	    	int[] dbArray = new int[anz];
	    	for(int i=0; i<anz;i++)
	    	{
	    		dbArray[i] = tmpGruppen[i].id;
	    	}
	    	Arrays.sort(dbArray);
	    	if(Arrays.equals(copy,dbArray)==true)
	    	{
	    		for(int j=0;j<anz;j++)
	    		{
	    			//server boolean aendernReihenfolge(int GID, String Reihenfolge)throws RemoteException
	    			server.aendernReihenfolge(reihenfolge_GID[0], String.valueOf(j+1));
	    		}return 0;
	    	}return 1;
	    	
	    	
	    	
	    	
	    }else return -9;
	}
    	    //Scanner sc = new Scanner(System.in);
    	    //erst alle Gruppen mit reihenfolge anzeigen
    	    /*int anz= server.anzeigenGruppen().length;
        	Gruppe[] tmpGruppen = new Gruppe[anz];
        	for(int i=0;i<anz;i++)
        	{			
        		System.out.println("Id: " + tmpGruppen[i].id);
        		System.out.println("Gruppenname: " + tmpGruppen[i].name);
        		System.out.println("Reihenfolge: " +tmpGruppen[i].reihenfolge);
        	}
        	System.out.println("Bitte geben Sie die neue Reihenfolge an");
        	for(int i=0;i<anz;i++)
        	{			
        		System.out.println("Id: " + tmpGruppen[i].id);
        		System.out.println("Gruppenname: " + tmpGruppen[i].name);
        		System.out.println("Neue Reihenfolge?: ");
        		String reihenfolge_new = sc.next();
        		if( server.aendernReihenfolge(tmpGruppen[i].id, reihenfolge_new))
        		{
        		    System.out.println("Reihenfolge erfolgreich geaendert");
        		    //Reihenfolge wurde erfolgreich ge?ndert-->neue Zyklustermine eintragen
        		    //1. aktuelles DAtum herausfinden
        		    String DATE_FORMAT = "yyyy-MM-dd";
        		    java.util.Date date = new java.util.Date();
        	        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        	        Termin tmpTermin = new Termin();
        	        tmpTermin.datum = sqlDate;
        	        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                    String sDate = sdf.format(sqlDate);
        	        boolean ret = server.loeschenAbHier(sDate);
        	        boolean ret2 =server.einfuegenAbHier();
        	        if( ret == false || ret2 == false)
        	        {
        	            return 1;
        	            //System.out.println("Fehler beim EinfÃ¼gen der neuen Termine");
        	            //eig Rollback des LÃ¶schens und so
        	        }
        	        //System.out.println("Termine wurden erfolgreich eingetragen");
        	        //Nachricht an alle: "Reihenfolge wurde geÃ¤ndert; neue Termine
        		}
        		else return 2;
        		//System.out.println("Fehler beim Aendern der Reihenfolge");
        	}         	        
        	return 0;
	    }
	    return -9;
	}*/
	
//	http://oreilly.com/catalog/javarmi/chapter/ch10.html
// http://openbook.galileocomputing.de/javainsel-9/javainsel_17_010.htm#mj-9d80b-91a6c1-9f204a-9-9e533d4b06d-9-98

	public int einfuegenAuZykTermin(java.sql.Date Datum, java.sql.Time zeit, String ort, String essen, int GruppenID) throws RemoteException
	{
		if(AktNutzer.rechte==ADMIN)
		{
			int ret = pruefenLaenge(ort,laenge_ort);
			if(ret == 1)
				return 2;
			int ret2 = pruefenLaenge(essen,laenge_essen);
			if(ret2 == 1)
				return 3;
			
			java.util.Date date = new java.util.Date();			//gibt das aktuelle Datum
    		//getMonth()== ... getYear()== ... getDay() - currentDay >=3
    	    java.sql.Date sqlDate = new java.sql.Date(date.getDate());	//wandelt aktu Datum von util.Date in sql.Date um
    	    int dayDiff = Datum.getDate() - sqlDate.getDate();
    	    int monthDiff =Datum.getMonth() -sqlDate.getMonth();
    	    int yearDiff = Datum.getYear() -sqlDate.getYear();
    	    
    	    
    	    if(dayDiff >=3 && monthDiff >=0 && yearDiff>=0)
			{
				int einfret=server.einfuegenAuZykTermin(GruppenID, Datum,zeit,ort,essen);
					return einfret;//-1 Termin gibts schon; -2 Fehler
			}else return 4;//keine 3 Tage hin bis zu diesen Termin
    	    
		}else return -9;
	}
		/*
		//Scanner sc = new Scanner(System.in);
		Termin tmpTermin = new Termin();
		//System.out.println("Geben Sie das Datum des neuen Termin ein");
	    /*String DATE_FORMAT = "yyyy-MM-dd";
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    //anfang eingabe in datum (sql.date) umwandeln
	    java.util.Date date=null;
		try {
			date = sdf.parse(sc.next());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    tmpTermin.datum = new Date(date.getTime());
	    //ende string in datum
	    System.out.println("Geben Sie an zu welcher Uhrzeit der neue Termin sein soll");
	    //anfang string to time
	    String TIME_FORMAT = "hh:mm:ss";
	     /* 
	    http://stackoverflow.com/questions/2731443/cast-a-string-to-sql-time
	    String s = /* your date string here ;
		SimpleDateFormat sdf = new SimpleDateFormat( your date format string here );
		long ms = sdf.parse(s).getTime();
		Time t = new Time(ms);
	     
	    String time = sc.next();
	    SimpleDateFormat sdf2 = new SimpleDateFormat(TIME_FORMAT);
	    long ms=0;
		try {
			ms = sdf2.parse(time).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    tmpTermin.zeit = new Time(ms);
	    //ende string to time
	    System.out.println("Geben Sie an welche Gruppe (GruppenID) ausrichten soll");
	    tmpTermin.gruppe = Integer.parseInt(sc.next());
	    System.out.println("Geben Sie den Ort des neuen Termin ein");
	    tmpTermin.ort = sc.next();
	    //aufrufen der serverfkt zum einfï¿½gen
	    int ret = server.einfuegenAuZykTermin(tmpTermin);
	    if( ret == -1)
	    	return 2;
	    	//System.out.println("Zu diesen Datum existiert bereits ein Termin");
	    else 
	    	if( ret == -2)
	    		return 1;
	    //System.out.println("Es ist ein Fehler beim Eintragen aufgetreten");
	    	else return 0;
	    		//System.out.println("Der neue Termin wurde mit der Termin_ID" +ret+"eingetragen.");
	    //Nachricht an alle
	}*/
	
	public int aussetzenTermin(int TerminID) throws RemoteException
	{
		if(AktNutzer.rechte == GLEITER)
		{
			if(server.aussetzenTermin(TerminID))
				return 0;
			return 1;
		}else return -9;
	}
			//verschieben um eine Woche
			/*Scanner sc = new Scanner(System.in);
			//System.out.println("Wollen Sie den nï¿½chsten Termin aussetzen? (1) Oder wollen Sie einen Termin eingeben? (2)");
			int Entscheidung = Integer.parseInt(sc.next());
			switch (Entscheidung)
			{
			case 1: if( server.aussetzenTermin()==true)
						System.out.println("Erfolg");
					else System.out.println("Kein Erfolg");
			break;
			case 2: //anzeigenTermine();//anzeigen aller? auch vergangene-->GUUUUUUI
					System.out.println("Termin Id eingeben, welcher Termin ausgesetzt werden soll:");
					int TerminID = Integer.parseInt(sc.next());
					Termin tmpTermin = server.anzeigenTermin(TerminID);
					String DATE_FORMAT = "yyyy-MM-dd";
	        		java.util.Date date = new java.util.Date();
	        	    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
					if(tmpTermin.gruppe==AktNutzer.gruppe && tmpTermin.datum.before(sqlDate))//ausgewï¿½hlter Termin gehï¿½rt zur eigenen Gruppe & und liegt nicht in der vergangenheit
				    {
						if (server.aussetzenTermin(TerminID)== true)
							return 0;
						//System.out.println("Erfolg");
						else 
							return 1;
						//System.out.println("Kein Erfolg");
				    } else return 2;
					//System.out.println("Fehler: entweder nicht dein Termin von der Gruppe oder Termin liegt in der Vergangenheit");
			
		   	default: System.out.println("Falsche Eingabe");
			}
		}
		else return -9;
		return -1;
	}*/
	
	public int ueberspringenTermin(int TerminID) throws RemoteException
	{
		if(AktNutzer.rechte == GLEITER)
		{
				int ret =server.ueberspringenTermin(TerminID);
					return ret;//0 erfolgreich; -1 fehler; >0 sonst ID des Termins der angefragt hat 
			
		}return -9;
	}
		
			/*Scanner sc = new Scanner(System.in);
			System.out.println("Welchen Termin wollen Sie ï¿½berspringen?");
			//anzeigenTermine();//anzeigen aller? auch vergangene-->GUUUUUI
			System.out.println("Termin Id eingeben, welcher Termin ï¿½berjumpt werden soll:");
			int TerminID = Integer.parseInt(sc.next());
			Termin tmpTermin = server.anzeigenTermin(TerminID);
			String DATE_FORMAT = "yyyy-MM-dd";
    		java.util.Date date = new java.util.Date();
    	    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			if(tmpTermin.gruppe==AktNutzer.gruppe && tmpTermin.datum.before(sqlDate))//ausgewï¿½hlter Termin gehï¿½rt zur eigenen Gruppe & und liegt nicht in der vergangenheit
		    {
				int ret = server.ueberspringenTermin(TerminID);
				//if (ret!=-1)
					return ret;
				//System.out.println("Erfolg");
				//else 
					//return 1;
				//System.out.println("Kein Erfolg");
		    } else return 1;
			//System.out.println("Fehler: entweder nicht dein Termin von der Gruppe oder Termin liegt in der Vergangenheit");
		}
		else return -9;
	}*/
	
	public int erstellenTauschanfrage(int TerminID_quelle, int TerminID_ziel) throws RemoteException
	{
		/*
		1) eigenen Termin auswï¿½hlen
		1.2) prï¿½fen ob 3 Tage vorher
		2) zu tauschenden Termin auswï¿½hlen
		3) prï¿½fen ob schon getauscht-->dbapi.isGetauscht(TerminID)
		4) prï¿½fen ob bereits Tauschanfragen existieren-->getAllTauschanfragen-->filtern nach TerminID(tauschanfrage.neu)-->wenn anzahl==0 dann nein ansonsten ja
		5) wenn nein gehe zu 7)
		6) wenn ja Ausgabe: jo hey gibt schon Tauschanfrage abbruch
		7) tauschanfrage eintragen in DB
		8) auf bestï¿½tigung warten
		-9) wenn ja gehe zu 11)
		10) wenn nein Meldung: will net-->tauschanfrage wieder lï¿½schen
		11) Termin tauschen
*/
		if(AktNutzer.rechte == GLEITER)
		{
			int ret = server.erstellenTauschanfrage(TerminID_quelle, TerminID_ziel);
			return ret;//-3 Termin_ziel schon getuascht; -2 Fehler; -1 TA gibts schon; ansonsten TauschanfragenID
		}else return -9;
	}
			/*Scanner sc = new Scanner(System.in);
			//anzeigenTermine();//GUUUUUUUI
			System.out.println("GEben Sie die ID an welchen Termin Sie tauschen wollen");
			int TerminID = Integer.parseInt(sc.next());
			Termin tmpTermin = server.anzeigenTermin(TerminID);
			String DATE_FORMAT = "yyyy-MM-dd"; //das before() ist auch util.date ... sql.date hat nur rotzfunltionen ... wie isFist() oder na toll aber alle daten von Termin ist nun mal sql
    		java.util.Date date = new java.util.Date();
    		//getMonth()== ... getYear()== ... getDay() - currentDay >=3
    	    java.sql.Date sqlDate = new java.sql.Date(date.getDate());
    	    int dayDiff = tmpTermin.datum.getDate() - sqlDate.getDate();
    	    int monthDiff = tmpTermin.datum.getMonth() -sqlDate.getMonth();
    	    int yearDiff = tmpTermin.datum.getYear() -sqlDate.getYear();
    	    if(server.existTauschanfrage(TerminID))
    	    {
    	    	System.out.println("Sorry hierzu gibts bereits ne Tauschanfrage");
    	    	return 2;
    	    }
    	    if(tmpTermin.istgetauscht==true)
			{
				System.out.println("Sorry dieser Termin wurde bereits getauscht");
				return 3;
			}
			if(tmpTermin.gruppe==AktNutzer.gruppe && dayDiff >=3 && monthDiff==0 && yearDiff==0)//ausgewï¿½hlter Termin ist eigener und es ist noch mind. 3 Tage hin
			{
				//anzeigen aller Termine der anderen Gruppe
				//anzeigenTermine();
				System.out.println("Wï¿½hlen SIe den Termin aus mit dem getauscht werden soll");
				int TerminID_ziel = Integer.parseInt(sc.next());
				if(server.existTauschanfrage(TerminID_ziel))
				{
					System.out.println("Sorry hierzu gibts bereits ne Tauschanfrage");
	    	    	return 4;
				}
				Termin tmpTermin_ziel = server.anzeigenTermin(TerminID);
				if(tmpTermin_ziel.istgetauscht==true)
				{
					System.out.println("Sorry dieser Termin wurde bereits getauscht");
					return 5;
				}
				int ret = server.insertTauschanfrage(TerminID, TerminID_ziel);
				//if(ret>=0)
					//{
						//System.out.println("Tauschanfrage erfolgreich eingetragen");
						//TODO: Nachricht an anderen hey Tauschanfr
						//return 0;
					//}
				//else return 1; //Fehler beim eintragen
						return ret;
			}
		}else return -9;
		return -1;
	}*/
	
	public Tauschanfrage[] anzeigenTauschanfragenGruppe() throws RemoteException
	{
		if(AktNutzer.gruppe==GLEITER)
		{
			int anz = 0;
			anz = server.anzeigenTauschanfragenGruppe(AktNutzer.gruppe).length;
			Tauschanfrage[] tmpTA = new Tauschanfrage[anz];	
			tmpTA = server.anzeigenTauschanfragenGruppe(AktNutzer.gruppe);
			if(tmpTA==null)
			{
				return null;
			}else return tmpTA;
		}else return null;
	}
	
	public int bearbeitenTauschanfrage(int TA_ID, boolean antwort) throws RemoteException
	{
		if(AktNutzer.rechte == GLEITER)
		{
			if(antwort==true)
			{
				if(server.executeTauschanfrage(TA_ID))
					return 0;
				else return 1;
			}
			else 
			{
				if(server.deleteTauschanfrage(TA_ID))
					return 0;
				else return 1;
			}
		}else return -9;
	}
			/*int anz = 0;
			try {
				anz = server.anzeigenTauschanfragenGruppe(AktNutzer.gruppe).length;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Tauschanfrage[] tmpTA = new Tauschanfrage[anz];
			try {
				tmpTA = server.anzeigenTauschanfragenGruppe(AktNutzer.gruppe);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(anz>0)
			{   //speichern aller moeglichen TauschIDs fuer diese Gruppe
				
				Scanner sc = new Scanner(System.in);
				System.out.println("Welche ID wollen Sie bearbeiten?");
				int TaID = Integer.parseInt(sc.next());
				boolean gefunden=false;
				for(int j=0;j<anz;j++)
				{
					if(TaID==tmpTA[j].id)//gefunden
					{
					  gefunden=true;	
					}
				}
				if(gefunden==true)
				{
					//System.out.println("Tausch bestaetigen: "Y" Tausch ablehnen: "N"  ?");
					 String antwort = sc.next();
					 if( antwort.equals("Y") || antwort.equals("y"))
					 {
						 //es soll getauscht werden
						 try {
							if(server.executeTauschanfrage(TaID)==true)
							 {
								//System.out.println("Tausch erfolgreich.");
								//TODO: NAchricht ??? an ALLE?
								 
							 }
							 else
							 {
									//System.out.println("Fehler bei Tauschbestaetigung.");
								 return 1;
							 }
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
					 else
					 {
						 //es soll nicht getauscht werden
						 try {
							if(server.deleteTauschanfrage(TaID)==true)
							 {
							   return 0;
							 }
							 else
							 {
								//System.out.println("Fehler beim loeschen der Tauschanfrage.");
								 return 1;
								 
							 }
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					 }
					
				}
				else
				{
					//System.out.println("Eingegebene ID ist keine gueltige TauschanfragenID.");
					 return 2;
				}
			}
			else
			{
				//System.out.println("Keine Tauschanfragen fuer diese Gruppe");
				 return 3;
			}
		}
		else
		{
			//System.out.println("Keine Berechtigung die Fkt auszufuehren");
			 return -9;
		}
		return -1;		
  }*/
	 
	public Person[] anzeigenNichtMitglied() throws RemoteException {
		return server.anzeigenNichtMitglied();
	}
	
	public Person[] anzeigenMitglied(int GruppenID) throws RemoteException
	{
		return server.anzeigenMitglieder(GruppenID);
	}
	
	public Gruppe anzeigenGruppe(int id) throws RemoteException {
		return server.anzeigenGruppe(id);
	}
	
	public Gruppe[] anzeigenGruppen() throws RemoteException {
		return server.anzeigenGruppen();
	}
	
	public int pruefenLaenge(String wert, int maxLaenge)
	{
		int laenge = wert.length();
		if(laenge <= maxLaenge)
			return 0;
		else return 1;
	}
	
}
