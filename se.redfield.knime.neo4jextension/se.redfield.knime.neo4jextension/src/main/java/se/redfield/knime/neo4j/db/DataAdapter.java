/**
 *
 */
package se.redfield.knime.neo4j.db;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataType;
import org.knime.core.data.MissingCell;
import org.knime.core.data.blob.BinaryObjectCellFactory;
import org.knime.core.data.blob.BinaryObjectDataCell;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.ListCell;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.LongCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.time.duration.DurationCellFactory;
import org.knime.core.data.time.localdate.LocalDateCellFactory;
import org.knime.core.data.time.localdatetime.LocalDateTimeCellFactory;
import org.knime.core.data.time.localtime.LocalTimeCellFactory;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.TypeSystem;

/**
 * @author Vyacheslav Soldatov <vyacheslav.soldatov@inbox.ru>
 *
 */
public class DataAdapter {
    //type system cache
    private final TypeSystem typeSystem;

    public DataAdapter(final TypeSystem s) {
        super();
        this.typeSystem = s;
    }
    /**
     * @param value value
     * @return data cell.
     */
    public DataCell createCell(final Value value)
            throws Exception {
        if (value == null) {
            return null;
        }

        if (isBoolean(value)) {
            return value.asBoolean() ? BooleanCell.TRUE : BooleanCell.FALSE;
        } else if (isBytes(value)) {
            return new BinaryObjectCellFactory().create(value.asByteArray());
        } else if (isString(value)) {
            return new StringCell(value.asString());
        } else if (isInteger(value)) {
            return new IntCell(value.asInt());
        } else if (isFloat(value)) {
            return new DoubleCell(value.asDouble());
        } else if (isNumber(value)) {
            return new DoubleCell(value.asDouble());
        } else if (isList(value)) {
            return CollectionCellFactory.createListCell(createLiceCell(value.asList()));
        } else if (isMap(value)) {
            return new StringCell(value.toString());
        } else if (isNode(value)) {
            return new StringCell(value.toString());
        } else if (isRelationship(value)) {
            return new StringCell(value.toString());
        } else if (isPath(value)) {
            return new StringCell(value.toString());
        } else if (isPoint(value)) {
            return new StringCell(value.toString());
        } else if (isDate(value)) {
            return new LocalDateCellFactory().createCell(formatDate(value.asZonedDateTime()));
        } else if (isTime(value)) {
            return new LocalTimeCellFactory().createCell(formatTime(value.asZonedDateTime()));
        } else if (islocalTime(value)) {
            return new LocalTimeCellFactory().createCell(formatTime(value.asLocalTime()));
        } else if (isLocalDateTime(value)) {
            return new LocalDateTimeCellFactory().createCell(formatDate(value.asLocalDateTime()));
        } else if (isDateTime(value)) {
            return new LocalDateTimeCellFactory().createCell(formatDate(value.asLocalDateTime()));
        } else if (isDuration(value)) {
            return DurationCellFactory.create(
                    Duration.ofNanos(value.asIsoDuration().nanoseconds()));
        } else if (isNull(value)) {
            return new MissingCell("is null");
        }

        return new StringCell(value.toString());
    }
    private List<DataCell> createLiceCell(final List<Object> neo4JList) {
        final List<DataCell> list = new LinkedList<>();
        for (final Object obj : neo4JList) {
            list.add(createCellForObject(obj));
        }
        return list;
    }
    public DataType getCompatibleType(final Value value) {
        if (isBoolean(value)) {
            return BooleanCell.TYPE;
        } else if (isBytes(value)) {
            return BinaryObjectDataCell.TYPE;
        } else if (isString(value)) {
            return StringCell.TYPE;
        } else if (isInteger(value)) {
            return IntCell.TYPE;
        } else if (isFloat(value)) {
            return DoubleCell.TYPE;
        } else if (isList(value)) {
            return ListCell.getCollectionType(getFirstListElementType(value.asList()));
        } else if (isNumber(value)) {
            return DoubleCell.TYPE;
        } else if (isMap(value)) {
            return StringCell.TYPE;
        } else if (isNode(value)) {
            return StringCell.TYPE;
        } else if (isRelationship(value)) {
            return StringCell.TYPE;
        } else if (isPath(value)) {
            return StringCell.TYPE;
        } else if (isPoint(value)) {
            return StringCell.TYPE;
        } else if (isDate(value)) {
            return LocalDateCellFactory.TYPE;
        } else if (isTime(value)) {
            return LocalTimeCellFactory.TYPE;
        } else if (islocalTime(value)) {
            return LocalTimeCellFactory.TYPE;
        } else if (isLocalDateTime(value)) {
            return LocalDateTimeCellFactory.TYPE;
        } else if (isDateTime(value)) {
            return LocalDateTimeCellFactory.TYPE;
        } else if (isDuration(value)) {
            return DurationCellFactory.TYPE;
        } else if (isNull(value)) {
            return StringCell.TYPE;
        }

        return StringCell.TYPE;
    }
    public boolean isNull(final Value value) {
        return typeSystem.NULL().isTypeOf(value);
    }
    public boolean isDuration(final Value value) {
        return typeSystem.DURATION().isTypeOf(value);
    }
    public boolean isDateTime(final Value value) {
        return typeSystem.DATE_TIME().isTypeOf(value);
    }
    public boolean isLocalDateTime(final Value value) {
        return typeSystem.LOCAL_DATE_TIME().isTypeOf(value);
    }
    public boolean islocalTime(final Value value) {
        return typeSystem.LOCAL_TIME().isTypeOf(value);
    }
    public boolean isTime(final Value value) {
        return typeSystem.TIME().isTypeOf(value);
    }
    public boolean isDate(final Value value) {
        return typeSystem.DATE().isTypeOf(value);
    }
    public boolean isPoint(final Value value) {
        return typeSystem.POINT().isTypeOf(value);
    }
    public boolean isPath(final Value value) {
        return typeSystem.PATH().isTypeOf(value);
    }
    public boolean isRelationship(final Value value) {
        return typeSystem.RELATIONSHIP().isTypeOf(value);
    }
    public boolean isNode(final Value value) {
        return typeSystem.NODE().isTypeOf(value);
    }
    public boolean isMap(final Value value) {
        return typeSystem.MAP().isTypeOf(value);
    }
    public boolean isList(final Value value) {
        return typeSystem.LIST().isTypeOf(value);
    }
    public boolean isFloat(final Value value) {
        return typeSystem.FLOAT().isTypeOf(value);
    }
    public boolean isInteger(final Value value) {
        return typeSystem.INTEGER().isTypeOf(value);
    }
    public boolean isNumber(final Value value) {
        return typeSystem.NUMBER().isTypeOf(value);
    }
    public boolean isString(final Value value) {
        return typeSystem.STRING().isTypeOf(value);
    }
    public boolean isBytes(final Value value) {
        return typeSystem.BYTES().isTypeOf(value);
    }
    public boolean isBoolean(final Value value) {
        return typeSystem.BOOLEAN().isTypeOf(value);
    }

    public static String formatDate(final LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
    public static String formatTime(final LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
    public static String formatTime(final ChronoZonedDateTime<?> dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_TIME);
    }
    public static String formatDate(final ChronoZonedDateTime<?> dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private DataCell createCellForObject(final Object obj) {
        if (obj == null) {
            return new MissingCell("Is null");
        }

        if (obj instanceof String) {
            return new StringCell(obj.toString());
        }
        if (obj instanceof BigInteger || obj instanceof Long) {
            return new LongCell(((Number) obj).longValue());
        }
        if (obj instanceof Byte || obj instanceof Integer || obj instanceof Short) {
            return new IntCell(((Byte) obj).byteValue());
        }
        if (obj instanceof Float || obj instanceof Double) {
            return new DoubleCell(((Number) obj).doubleValue());
        }
        if (obj instanceof Collection) {
            final List list = new LinkedList((Collection) obj);
            return CollectionCellFactory.createListCell(createLiceCell(list));
        }

        return new StringCell(obj.toString());
    }
    private DataType getFirstListElementType(final List<Object> list) {
        if (list.isEmpty()) {
            return StringCell.TYPE;
        }

        final Object obj = list.get(0);
        if (obj instanceof String) {
            return StringCell.TYPE;
        }
        if (obj instanceof BigInteger || obj instanceof Long) {
            return LongCell.TYPE;
        }
        if (obj instanceof Byte || obj instanceof Integer || obj instanceof Short) {
            return IntCell.TYPE;
        }
        if (obj instanceof Float || obj instanceof Double) {
            return DoubleCell.TYPE;
        }

        return StringCell.TYPE;
    }
}