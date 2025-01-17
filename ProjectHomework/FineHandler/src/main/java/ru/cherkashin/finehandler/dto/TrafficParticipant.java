package ru.cherkashin.finehandler.dto;

public class TrafficParticipant {
    private int cameraId;
    private String carNumber;
    private int speed;
    private String photoId;

    public TrafficParticipant() {
    }

    public int getCameraId() {
        return this.cameraId;
    }

    public String getCarNumber() {
        return this.carNumber;
    }

    public int getSpeed() {
        return this.speed;
    }

    public String getPhotoId() {
        return this.photoId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TrafficParticipant)) return false;
        final TrafficParticipant other = (TrafficParticipant) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getCameraId() != other.getCameraId()) return false;
        final Object this$carNumber = this.getCarNumber();
        final Object other$carNumber = other.getCarNumber();
        if (this$carNumber == null ? other$carNumber != null : !this$carNumber.equals(other$carNumber)) return false;
        if (this.getSpeed() != other.getSpeed()) return false;
        final Object this$photoId = this.getPhotoId();
        final Object other$photoId = other.getPhotoId();
        if (this$photoId == null ? other$photoId != null : !this$photoId.equals(other$photoId)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TrafficParticipant;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getCameraId();
        final Object $carNumber = this.getCarNumber();
        result = result * PRIME + ($carNumber == null ? 43 : $carNumber.hashCode());
        result = result * PRIME + this.getSpeed();
        final Object $photoId = this.getPhotoId();
        result = result * PRIME + ($photoId == null ? 43 : $photoId.hashCode());
        return result;
    }

    public String toString() {
        return "TrafficParticipant(cameraId=" + this.getCameraId() + ", carNumber=" + this.getCarNumber() + ", speed=" + this.getSpeed() + ", photoId=" + this.getPhotoId() + ")";
    }
}
