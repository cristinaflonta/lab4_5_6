package ro.ubb.exam.Repository.XMLRepository;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Holiday;
import ro.ubb.exam.Domain.Reservation;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import org.w3c.dom.Document;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.InMemoryRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.util.Optional;

import org.w3c.dom.Element;


public class ReservationXmlRepository extends InMemoryRepository<Long, Reservation> {
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private TransformerFactory transformerFactory;
    private Transformer transformer;
    private String filePath;

    public ReservationXmlRepository(Validator<Reservation> validator, String filePath) {
        super(validator);
        this.filePath = filePath;
        try {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(filePath);
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        Element root = document.getDocumentElement();
        NodeList reservationNodeList = root.getChildNodes();
        for (int i = 0; i < reservationNodeList.getLength(); i++) {
            Node reservationNode = reservationNodeList.item(i);
            if (!(reservationNode instanceof Element)) {
                continue;
            }
            Element reservationElement = (Element) reservationNode;
            buildReservationFromElement(reservationElement);
        }
    }

    private void buildReservationFromElement(Element reservationElement) {
        Long id = Long.parseLong(getTextFromTagName(reservationElement, "Reservation-id"));
        Long clientId = Long.parseLong(getTextFromTagName(reservationElement, "clientId"));
        String clientName = getTextFromTagName(reservationElement, "clientName");
        String clientEmail = getTextFromTagName(reservationElement, "clientEmail");
        Long holidayId = Long.parseLong(getTextFromTagName(reservationElement, "holidayId"));
        String holidayName = getTextFromTagName(reservationElement, "holidayName");
        String holidayDestination = getTextFromTagName(reservationElement, "holidayDestination");
        Long holidayPrice = Long.parseLong(getTextFromTagName(reservationElement, "holidayPrice"));

        Client client = new Client(clientName, clientEmail);
        client.setId(id);
        Holiday holiday = new Holiday(holidayName, holidayDestination, holidayPrice);
        holiday.setId(id);
        Reservation Reservation = new Reservation(client, holiday);
        super.save(Reservation);
    }

    private String getTextFromTagName(Element reservationElement, String tagName) {
        return reservationElement.getElementsByTagName(tagName).item(0).getTextContent();
    }

    @Override
    public Optional<Reservation> save(Reservation entity) throws ValidatorException {
        Optional<Reservation> ReservationOptional = super.save(entity);
        saveToXml(entity);

        return ReservationOptional;
    }

    private void saveToXml(Reservation entity) {
        try {
            Element root = document.getDocumentElement();
            Node node = createNodeFromReservation(entity);
            root.appendChild(node);
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element createNodeFromReservation(Reservation entity) {
        Element reservationElement = document.createElement("Reservation");
        addChildWithTextContent(reservationElement, "reservation-id", String.valueOf(entity.getId()));
        addChildWithTextContent(reservationElement, "clientId", String.valueOf(entity.getClient().getId()));
        addChildWithTextContent(reservationElement, "clientName", entity.getClient().getName());
        addChildWithTextContent(reservationElement, "clientEmail", entity.getClient().getEmail());
        addChildWithTextContent(reservationElement, "holidayId", String.valueOf(entity.getHoliday().getId()));
        addChildWithTextContent(reservationElement, "holidayName", entity.getHoliday().getName());
        addChildWithTextContent(reservationElement, "holidayDestination", entity.getHoliday().getDestination());
        addChildWithTextContent(reservationElement, "holidayPrice", String.valueOf(entity.getHoliday().getPrice()));
        return reservationElement;
    }

    private void addChildWithTextContent(Element reservationElement, String tagName, String valueOf) {
        Element childElement = document.createElement(tagName);
        childElement.setTextContent(valueOf);
        reservationElement.appendChild(childElement);
    }

    @Override
    public Optional<Reservation> delete(Long aLong) {
        Optional<Reservation> ReservationOptional = super.delete(aLong);
        NodeList nodeList = document.getElementsByTagName("Reservation-id");
        removeReservationFromDom(aLong, nodeList);
        try {
            transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ReservationOptional;
    }

    private void removeReservationFromDom(Long aLong, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            Long id = Long.parseLong(node.getTextContent());
            if (aLong.equals(id)) {
                node.getParentNode().getParentNode().removeChild(node.getParentNode());
            }
        }
    }

    public Optional<Reservation> update(Reservation entity) throws ValidatorException {
        Optional<Reservation> ReservationOptional = super.update(entity);
        NodeList nodeList = document.getElementsByTagName("Reservation-id");
        removeReservationFromDom(entity.getId(), nodeList);
        saveToXml(entity);
        return ReservationOptional;

    }

}

