package ro.ubb.exam.Repository.XMLRepository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.ubb.exam.Domain.Holiday;
<<<<<<< HEAD
=======
import ro.ubb.exam.Domain.Entity;
>>>>>>> origin/master
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Domain.Validators.Validator;
import ro.ubb.exam.Repository.InMemoryRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Optional;

public class HolidayXmlRepository extends InMemoryRepository<Long, Holiday> {
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private TransformerFactory transformerFactory;
    private Transformer transformer;
    private String filePath;

    public HolidayXmlRepository(Validator<Holiday> validator, String filePath) {
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
        Element root = document.getDocumentElement();       //citire lista Holday,
        NodeList holidayNodeList = root.getChildNodes();
        for (int i = 0; i < holidayNodeList.getLength(); i++) {
            Node holidayNode = holidayNodeList.item(i);
            if (!(holidayNode instanceof Element)) { // verificare consistenta fisier xml
                continue;
            }
            Element holidayElement = (Element) holidayNode;
            buildHolidayFromElement(holidayElement);
        }
    }

    private void buildHolidayFromElement(Element holidayElement) {
        Long id = Long.parseLong(getTextFromTagName(holidayElement, "id"));
        String name = getTextFromTagName(holidayElement, "name");
        String destination = getTextFromTagName(holidayElement, "destination");
        Long price = Long.parseLong(getTextFromTagName(holidayElement, "price"));
        Holiday holiday = new Holiday(name, destination, price);
        holiday.setId(id);
        super.save(holiday);
    }

    private String getTextFromTagName(Element holidayElement, String tagName) {
        return holidayElement.getElementsByTagName(tagName).item(0).getTextContent();

    }

    @Override
    public Optional<Holiday> save(Holiday entity) throws ValidatorException {
        Optional<Holiday> holidayOptional = super.save(entity);
        saveToXml(entity);

        return holidayOptional;
    }

    private void saveToXml(Holiday entity) {
        try {
            Element root = document.getDocumentElement();
            Node node = createNodeFromHoliday(entity);
            root.appendChild(node);
            transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element createNodeFromHoliday(Holiday entity) {
        Element holidayElement = document.createElement("holiday");
        addChildWithTextContent(holidayElement, "id", String.valueOf(entity.getId()));
        addChildWithTextContent(holidayElement, "name", entity.getName());
        addChildWithTextContent(holidayElement, "destination", entity.getDestination());
        addChildWithTextContent(holidayElement, "price", String.valueOf(entity.getPrice()));
        return holidayElement;
    }

    private void addChildWithTextContent(Element holidayElement, String tagName, String valueOf) {
        Element childElement = document.createElement(tagName);
        childElement.setTextContent(valueOf);
        holidayElement.appendChild(childElement);
    }

    @Override
    public Optional<Holiday> delete(Long aLong) {
        Optional<Holiday> optionalHoliday = super.delete(aLong);
        NodeList nodeList = document.getElementsByTagName("id");
        removeHolidayFromDom(aLong, nodeList);
        try {
            transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return optionalHoliday;
    }


<<<<<<< HEAD
    private void removeHolidayFromDom(Long aLong, NodeList nodeList) {
=======
    private Optional<Holiday> removeHolidayFromDom(Long aLong, NodeList nodeList) {
>>>>>>> origin/master
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
<<<<<<< HEAD
    }
        @Override
        public Optional<Holiday> update(Holiday entity) throws ValidatorException {
            Optional<Holiday> holidayOptional = super.update(entity);
            NodeList nodeList= document.getElementsByTagName("id");
=======

        @Override
        public Optional<Holiday> update (Holiday entity) throws ValidatorException {
            Optional<Holiday> holidayOptional = super.update(entity);
            nodeList = document.getElementsByTagName("id");
>>>>>>> origin/master
            removeHolidayFromDom(entity.getId(), nodeList);
            saveToXml(entity);
            return holidayOptional;
        }
<<<<<<< HEAD
=======
    }
>>>>>>> origin/master
}