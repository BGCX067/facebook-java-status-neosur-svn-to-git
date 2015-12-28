package com.test;


import java.io.IOException;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.code.facebookapi.*;






/**
 * Servlet implementation class for Servlet: Publish
 *
 */
 public class Publish extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	static public long userID;

	static private final String apiKey ="caa8716fbe4315945a5152adff9a5a1f";
	static private final String secretKey = "76b6c27f1d96c07068b89bd5100d723d";


	public Publish() {
		super();

	}

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		Long uid = null;


		while (request.getAttributeNames().hasMoreElements()){
			System.out.println("At: " + request.getAttributeNames().nextElement().toString());
		}

		Enumeration<Object> a = request.getParameterNames();
		while (a.hasMoreElements()){
			System.out.println("PA: " + a.nextElement().toString());
		}


		FacebookRestClient frc = null;
		HttpSession session = request.getSession();
		String sessionKey = null;//(String) session.getAttribute("facebookSession");
		String token = request.getParameter("auth_token");



		frc = doLogin(response, request, apiKey, secretKey, token, session);




		if(frc!=null){

			/*USAMOS API FACEBOOK*/
			uid=frc.getUserId();
			userID=uid;
			System.out.println("------->User id: "+uid);


			try {
					//getFacebookInfo(request, frc,uid);//get profile info




				long bundleId=29325784559L;

				//frc.feed_publishUserAction(bundleId);
				String bodyGeneral = "Este es el Cuerpo de la publicacion" +
						"usando el template 299213099559, cargando desde" +
						"eclipse por medio de la app";
				int storySize=2;


				//Creamos el item para agregar a la info
				ProfileFieldItem item = new ProfileFieldItem("Titulo de info",
						"http://www.neosur.com.ar/FBPUBLISH/");
				item.setDescription("Esta es la descripcion de la info de " +
						"la app....");
				item.setSublabel("----->SubLABEL<-----");
				item.setImageUrl("http://gizmo5.com/images/download-linux2.gif");

				//Creamos una lista de items para agregar :P
				List<ProfileFieldItem> items = new ArrayList<ProfileFieldItem>();
				System.out.println(items.add(item));

				//Creamos un Campo de informacion para setear y le agreagamos su items
				ProfileInfoField field = new ProfileInfoField("--->Informacion<---");
				field.setItems(items);

				//Creamos una lista de los campos a setear
				List<ProfileInfoField> fields = new LinkedList<ProfileInfoField>();
				fields.add(field);

				frc.profile_setInfo(userID, "TItulo setINFO", true, fields);

				//Document doc = frc.profile_getInfo(userID);

				//frc.printDom(doc, "Debug en get info |");//TEST

//29325784559L;29213099559L;
				frc.feed_publishUserAction(bundleId, null, null , null, bodyGeneral, storySize);
				//frc.feed_publishTemplatizedAction("---->PUBLISHTemplatizedAction" +
					//	"<----");



				/*
				 * Subo una foto al wall de facebook si esta autorizado*/
				 /*if (frc.users_hasAppPermission(Permission.PHOTO_UPLOAD)) {
					  System.out.println("------->APP_PERMISION: TRUE");

					  File photo = new File("/home/gaston/Desktop/neosur.jpg");


					frc.photos_upload(photo);
				    }else{
				    	//response.sendRedirect("http://www.facebook.com/authorize.php?api_key="+ apiKey + "&v=1.0&ext_perm=status_update" );
				    	response.sendRedirect(Permission.authorizationUrl(apiKey, Permission.PHOTO_UPLOAD));

				    }*/



				/*get_Friends_Uid(frc, request, response);//get friends uid
				System.out.println("------->APP_PERMISION: " + uid);*/



			} catch (FacebookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}

	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	//--------------------------------------------->Logeo y autenticacion

	protected FacebookRestClient doLogin(HttpServletResponse response, ServletRequest request, String apiKey, String secretKey, String token, HttpSession session) throws IOException{
		FacebookRestClient frc = null;
		String sessionKey = null;


		System.out.println("auth_token: "+token);

			if (sessionKey != null && sessionKey.length() > 0) {

			frc = new FacebookRestClient(apiKey, secretKey, sessionKey);


			} else if (token != null) {

			session.setAttribute("auth_token", token);
			frc = new FacebookRestClient(apiKey, secretKey);
			frc.setIsDesktop(false);

			try{
				sessionKey = frc.auth_getSession(token);
				session.setAttribute("facebookSession", sessionKey);

				//frc.setDebug(true);

				}catch(FacebookException e){
				e.printStackTrace();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
			response.sendRedirect("http://www.facebook.com/login.php?api_key=" + apiKey + "&v=1.0");
			return null;
			}
		return frc;
	}




	//--------------------------------------------->Usos de facebook


	protected void 	get_Friends_Uid(FacebookRestClient client, HttpServletRequest req, HttpServletResponse res)
	throws IOException, FacebookException, ServletException {

		Document root = (Document)client.friends_get();
		NodeList list = root.getElementsByTagName("uid");

		client.printDom(root, "Debug en loginSucessfull |");//TEST

		ArrayList friends = new ArrayList();

		for(int i=0; i<list.getLength(); ++i) {
			String uid = list.item(i).getFirstChild().getNodeValue();
			friends.add(uid);

		}

		req.setAttribute("uid_friends", friends);
		System.out.print(friends);
		getServletContext().getRequestDispatcher("/main_page.jsp").forward(req, res);

		//long userid = Integer.valueOf((String) friends.get(0));

        //System.out.println(userid);

		//return userid;
	}




	 protected static boolean getFacebookInfo(
	         HttpServletRequest request,
	         FacebookRestClient facebook, long userID)
	   {

		   try {

		         try{System.out.println("*0*-----------getFacebookinfo------------*0*");

		        	 System.out.println(userID);
		         }catch(Exception e){
		        	 System.out.
		        	 println("**-----------error trying getFacebookinfo" +
		        	 		"------------**");}

		         ArrayList<Long> users = new ArrayList<Long>();
		         users.add(userID);


		         System.out.println("**-----------try getFacebookinfo------------**");

		         EnumSet<ProfileField> fields = EnumSet.of (
		        		 com.google.code.facebookapi.ProfileField.NAME,
		        		 com.google.code.facebookapi.ProfileField.PIC);

		         Document d = facebook.users_getInfo(users, fields);//facebook.users_getStandardInfo(users, fields);

		        // facebook.printDom(d, "Debug en get info |");//TEST


		         String name =
		            d.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();//.getLocalName();
		         String picture =
		            d.getElementsByTagName("pic").item(0).getFirstChild().getNodeValue();//.getLocalName();




		         request.setAttribute("uid", userID);
		         request.setAttribute("profile_name", name);
		         request.setAttribute("profile_picture_url", picture);


		      } catch (FacebookException e) {

		    	  System.out.println("**-----------error en GFI------------**");
		         HttpSession session = request.getSession();
		         session.setAttribute("facebookSession", null);
		         return false;

		      } catch (IOException e) {

		         e.printStackTrace();
		         return false;
		      }
		      return true;

	   }
}
