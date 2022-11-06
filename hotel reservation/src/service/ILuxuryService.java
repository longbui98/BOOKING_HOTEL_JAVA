package service;

public interface ILuxuryService {
    default void addOptional(){
        System.out.println("Please add optional you want when booking this room");
    }
}
