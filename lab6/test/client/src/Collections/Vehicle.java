package Collections;

import java.io.*;
import java.util.Date;

/**
 * Модель объекта "Транспорт"
 * Содержит геттеры/сеттеры каждого поля класса
 * Некоторые поля имеют ограничения. Они подписаны
 *
 * @since 1.0
 */

public class Vehicle implements Comparable<Vehicle>, Serializable {
    private static final long serialVersionUID = -6542558272882305629L;

    /**
     * Значение поля должно быть больше 0, Значение этого поля должно быть уникальным,
     * Значение этого поля должно генерироваться автоматически при помощи IdGenerator
     *
     * @since 1.0
     */
    private long id;
    /**
     * Название транспортного средства
     * Поле не может быть null, Строка не может быть пустой
     *
     * @since 1.0
     */
    private String name;
    /**
     * Координаты
     * Поле не может быть null
     *
     * @see Coordinates
     * @since 1.0
     */
    private Coordinates coordinates;
    /**
     * Дата создания транспортного средства
     * Поле не может быть null, Значение этого поля должно генерироваться автоматически
     *
     * @since 1.0
     */
    private java.util.Date creationDate;
    /**
     * Значение мощности двигателя
     * Поле не может быть null, Значение поля должно быть больше 0
     *
     * @since 1.0
     */
    private Integer enginePower;
    /**
     * Тип транспортного средства
     *
     * @see VehicleType
     * @since 1.0
     */
    private VehicleType type;
    /**
     * Тип топлива транспортного средства
     *
     * @see FuelType
     * @since 1.0
     */
    private FuelType fuelType;

    /**
     * Базовый пустой конструктор
     *
     * @since 1.0
     */
    public Vehicle() {

    }

    /**
     * Конструктор с заданными полями
     *
     * @since 1.0
     */
    public Vehicle(long id, String name, Coordinates coordinates, java.util.Date creationDate, Integer enginePower, VehicleType type, FuelType fuelType) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.enginePower = enginePower;
        this.type = type;
        this.fuelType = fuelType;
    }

    public Vehicle(String[] data) throws Exception {
        this.id = 0L;
        this.name = data[0];
        this.coordinates = new Coordinates(Long.parseLong(data[1]), Integer.parseInt(data[2]));
        this.creationDate = new Date(0);
        this.enginePower = Integer.parseInt(data[3]);
        this.type = VehicleType.valueOf(data[4]);
        this.fuelType = FuelType.valueOf(data[5]);
    }

    public byte[] objToByte() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(this);
        return byteStream.toByteArray();
    }

    public Object byteToObj(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objStream = new ObjectInputStream(byteStream);
        return objStream.readObject();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public Integer getEnginePower() {
        return enginePower;
    }

    public VehicleType getType() {
        return type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Вторая версия сеттера для ID
     * Используется для обновления полей элемента, без изменения ID
     *
     * @since 1.0
     */
    public void setIdForUpdate(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setEnginePower(Integer enginePower) {
        this.enginePower = enginePower;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public int compareTo(Vehicle o) {
        return (int) (this.id - o.getId());
    }

    @Override
    public String toString() {
        String result = String.format("Id: %d\nName: %s\nCoordinates: {x: %d, y: %d}\nCreation Time: %s\nEnginePower: %d\nVehicleType: %s\nFuelType: %s\n",
                getId(), getName(), getCoordinates().getX(), getCoordinates().getY(), getCreationDate(), getEnginePower(), getType(), getFuelType());
        return result;
    }
}

