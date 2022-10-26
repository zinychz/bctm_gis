package bctm.gis.mvc;

import gis.factory.Graph;
import gis.factory.ShapeFactory;
import gis.orm.Gisgraphs;
import gis.orm.GisgraphsDao;

import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;

@Named("gis")
// @SessionScoped
// @ViewScoped
@Scope("session") // need this, JSR-330 in Spring context is singleton by
					// default
public class GisBean implements Serializable {
	@Inject
	@Named("gisDao")
	private GisgraphsDao gisDao;

	@Inject
	@Named("shapeFactory")
	private ShapeFactory shapeFactory;

	@Inject
	@Named("editor")
	private EditorBean editorBean;

	@Inject
	@Named("mapFind")
	private MapFindBean mapFindBean;

	@Inject
	@Named("sendEmail")
	private Boolean sendEmail;

	@PostConstruct
	public void init() {
		editorBean.setGisBean(this);

//		if (sendEmail) {
//			
//			System.out.println("-----------------------------------------------");
//
//			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
//					.getRequest();
//
//			Map<String, String> map = new HashMap<String, String>();
//
//			Enumeration headerNames = request.getHeaderNames();
//			while (headerNames.hasMoreElements()) {
//				String key = (String) headerNames.nextElement();
//				String value = request.getHeader(key);
//				map.put(key, value);
//			}
//			
//
//			// Send Email. See:
//			// http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
//
//			// Use TLS:
//
//			Properties props = new Properties();
//			props.put("mail.smtp.host", "smtp.gmail.com");
//			props.put("mail.smtp.socketFactory.port", "465");
//			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//			props.put("mail.smtp.auth", "true");
//			props.put("mail.smtp.port", "465");
//
//			// Or use SSL:
//			// Properties props = new Properties();
//			// props.put("mail.smtp.host", "smtp.gmail.com");
//			// props.put("mail.smtp.socketFactory.port", "465");
//			// props.put("mail.smtp.socketFactory.class",
//			// "javax.net.ssl.SSLSocketFactory");
//			// props.put("mail.smtp.auth", "true");
//			// props.put("mail.smtp.port", "465");
//			
//			System.out.println("Before session");
//
//			Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication("zinychz", "************");
//				}
//			});
//			
//			System.out.println("After session = " + session);
//
//			try {
//
//				Message message = new MimeMessage(session);
//				message.setFrom(new InternetAddress("bctmGis@no-spam.com"));
//				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("zinychz@gmail.com"));
//				message.setSubject("Bctm Gis User Info");
//				message.setText(map.toString());
//
//				System.out.println("Before Transport.send(message)");
//				Transport.send(message);
//
//				System.out.println("Done send email");
//
//			} catch (MessagingException e) {
//				// throw new RuntimeException(e);
//				System.out.println("Error send email, see error log");
//				System.out.println(e.getMessage());
//				e.printStackTrace();
//			}
//
//			// System.out.println("|------------------header----------------------");
//			// System.out.println(map);
//			// System.out.println("------------------header----------------------|");
//		}
//		else{
////			System.out.println("");
//		}
	}

	private List<Gisgraphs> listAll;

	private String windowInfo;

	public String getWindowInfo() {
		return windowInfo;
	}

	public void setWindowInfo(String windowInfo) {
		this.windowInfo = windowInfo;
	}

	public List<String> getShapes() {
		List<String> shapes = new ArrayList<String>();
		if (listAll == null) {
			listAll = gisDao.getAll();
		}

		for (Gisgraphs g : listAll) {
			Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(), g.getName(), g.getCoord(),
					true);

			if (shape != null) {
				shapes.add(shape.getWrapLinkBody());
			}
		}

		return shapes;
	}

	public List<String> getShapesIntersect() {
		List<String> shapes = new ArrayList<String>();
		if (listAll == null) {
			listAll = gisDao.getAll();
		}

		Gisgraphs gCurrent = null;
		int gCurrentIndex = -1;
		boolean isRemove = false;
		if ((editorBean.getCurrentMode().equals(EditorBean.Mode.EDIT)
				|| editorBean.getCurrentMode().equals(EditorBean.Mode.DELETE)) && mapFindBean.getCurrentId() > 0) {

			Iterator<Gisgraphs> iterator = listAll.iterator();
			while (iterator.hasNext()) {
				gCurrentIndex++;
				if (iterator.next().getId() == mapFindBean.getCurrentId()) {
					gCurrent = listAll.remove(gCurrentIndex);
					isRemove = true;
					break;
				}
			}
		}

		if (windowInfo != null && !windowInfo.equals("")) {
			String[] arrWindowInfo = windowInfo.split(",");
			if (arrWindowInfo.length > 4) {
				double ltX = Double.parseDouble(arrWindowInfo[0]);
				double ltY = Double.parseDouble(arrWindowInfo[1]);
				double rbX = Double.parseDouble(arrWindowInfo[2]);
				double rbY = Double.parseDouble(arrWindowInfo[3]);
				double scale = Double.parseDouble(arrWindowInfo[4]);
				Area area = new Area(new Rectangle2D.Double(ltX, ltY, rbX - ltX, rbY - ltY));

				for (Gisgraphs g : listAll) {
					Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(), g.getName(),
							g.getCoord(), true);
					if (shape != null && shape.isShowBodyForScale(scale)) {
						double x = shape.getLtPoint().getX();
						double y = shape.getLtPoint().getY();
						double w = shape.getRbPoint().getX() - x;
						double h = shape.getRbPoint().getY() - y;
						boolean testIntersects = area.intersects(x, y, w, h);
						if (testIntersects) {
							if (editorBean.getCurrentMode().equals(EditorBean.Mode.NEW)) {
								shapes.add(shape.getBody(scale));
							} else {
								shapes.add(shape.getWrapLinkBody(scale));
							}
						}
					}
				}
			}
		}

		if (isRemove) {
			listAll.add(gCurrentIndex, gCurrent);
		}

		return shapes;
	}

	public List<String> getLabelsIntersect() {
		List<String> labels = new ArrayList<String>();
		if (listAll == null) {
			listAll = gisDao.getAll();
		}

		Gisgraphs gCurrent = null;
		int gCurrentIndex = -1;
		boolean isRemove = false;
		if (editorBean.getCurrentMode().equals(EditorBean.Mode.EDIT) && mapFindBean.getCurrentId() > 0) {

			Iterator<Gisgraphs> iterator = listAll.iterator();
			while (iterator.hasNext()) {
				gCurrentIndex++;
				if (iterator.next().getId() == mapFindBean.getCurrentId()) {
					gCurrent = listAll.remove(gCurrentIndex);
					isRemove = true;
					break;
				}
			}
		}

		if (windowInfo != null && !windowInfo.equals("")) {
			String[] arrWindowInfo = windowInfo.split(",");
			if (arrWindowInfo.length > 4) {
				double ltX = Double.parseDouble(arrWindowInfo[0]);
				double ltY = Double.parseDouble(arrWindowInfo[1]);
				double rbX = Double.parseDouble(arrWindowInfo[2]);
				double rbY = Double.parseDouble(arrWindowInfo[3]);
				double scale = Double.parseDouble(arrWindowInfo[4]);
				Area area = new Area(new Rectangle2D.Double(ltX, ltY, rbX - ltX, rbY - ltY));

				for (Gisgraphs g : listAll) {
					Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(), g.getName(),
							g.getCoord(), true);
					if (shape != null && shape.isShowLabelForScale(scale)) {
						double x = shape.getLtPoint().getX();
						double y = shape.getLtPoint().getY();
						double w = shape.getRbPoint().getX() - x;
						double h = shape.getRbPoint().getY() - y;
						boolean testIntersects = area.intersects(x, y, w, h);
						// if(g.getId() == 4715L)testIntersects = false;
						if (testIntersects) {
							if (editorBean.getCurrentMode().equals(EditorBean.Mode.NEW)) {
//								labels.add(shape.getLabel(scale));
							} else {
								labels.add(shape.getWrapLinkLabel(scale));
							}
						}
					}
				}
			}
		}

		if (isRemove) {
			listAll.add(gCurrentIndex, gCurrent);
		}

		return labels;
	}

	public List<String> getLabels() {
		List<String> labels = new ArrayList<String>();
		if (listAll == null) {
			listAll = gisDao.getAll();
		}

		for (Gisgraphs g : listAll) {
			Graph shape = shapeFactory.createShape(g.getId(), g.getType(), g.getPriority(), g.getName(), g.getCoord(),
					true);

			if (shape != null) {
				labels.add(shape.getWrapLinkLabel());
			}
		}

		return labels;
	}

	public List<Gisgraphs> getListAll() {
		return listAll;
	}

	public void setListAll(List<Gisgraphs> listAll) {
		this.listAll = listAll;
	}

	// @Autowired
	public GisBean(GisgraphsDao gisDao, ShapeFactory shapeFactory) {
		this.gisDao = gisDao;
		this.shapeFactory = shapeFactory;
	}

	public GisBean() {
	}
}
