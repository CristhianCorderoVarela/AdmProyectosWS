package cr.ac.una.admproyectosws.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
// Avisa a JAXB qué tipos de elementos puede encontrar dentro de la lista:
@XmlSeeAlso({ ProyectoDto.class })
public class RespuestaGeneralLista<T> extends RespuestaGeneral<List<T>> {

    // Sobrescribimos getter para declarar EXPRESAMENTE que es una LISTA y cómo serializarla
    @Override
    @XmlElementWrapper(name = "items")     // <items> ... </items>
    @XmlElement(name = "item")             // cada elemento <item>...</item>
    public List<T> getData() {
        return super.getData();
    }

    @Override
    public void setData(List<T> data) {
        super.setData(data);
    }
}
