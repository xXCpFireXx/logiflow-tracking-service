package co.com.bancolombia.model.tracking;

public class HistoryStep {
    private final String date;
    private final String time;
    private final String status;
    private final String location;
    private final String description;
    private final boolean active;
    private final boolean completed;

    public HistoryStep(String date, String time, String status, String location, String description, boolean active, boolean completed) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.location = location;
        this.description = description;
        this.active = active;
        this.completed = completed;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public boolean isCompleted() { return completed; }
}