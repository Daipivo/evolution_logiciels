package ExtractClass;

public class Smartphone {
	
    private String brand; // Marque du téléphone
    private String model; // Modèle du téléphone
    private Battery data = new Battery();

	public Smartphone(String brand, String model, int batteryCapacity, int batteryHealth) {
        this.brand = brand;
        this.model = model;
        this.data.batteryCapacity = batteryCapacity;
        this.data.batteryHealth = batteryHealth;
    }

    // Getters et Setters
    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getBatteryCapacity() {
        return data.batteryCapacity;
    }

    public int getBatteryHealth() {
        return data.batteryHealth;
    }
    
    public String toString() {
    	return "Brand : " + getBrand() + " model : " + getModel() + " battery capacity : " + getBatteryCapacity() + " battery health : " + getBatteryHealth();
    }
}
