package com.example.parkinglot.config;

import com.example.parkinglot.entity.ParkingTicket;
import com.example.parkinglot.entity.Vehicle;
import com.example.parkinglot.entity.Zone;
import com.example.parkinglot.enums.VehicleType;
import com.example.parkinglot.repository.ParkingTicketRepository;
import com.example.parkinglot.repository.VehicleRepository;
import com.example.parkinglot.repository.ZoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ZoneRepository zoneRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingTicketRepository parkingTicketRepository;

    public DataInitializer(ZoneRepository zoneRepository,
                           VehicleRepository vehicleRepository,
                           ParkingTicketRepository parkingTicketRepository) {
        this.zoneRepository = zoneRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingTicketRepository = parkingTicketRepository;
    }

    @Override
    public void run(String... args) {
        if (zoneRepository.count() == 0 && vehicleRepository.count() == 0) {
            // 1. Tạo các khu vực đỗ xe (Zone)
            Zone zoneA = Zone.builder().name("Khu A").capacity(50).occupiedSpots(1).build();
            Zone zoneB = Zone.builder().name("Khu B").capacity(30).occupiedSpots(1).build();
            zoneRepository.saveAll(List.of(zoneA, zoneB));

            // 2. Tạo các phương tiện (Vehicle)
            Vehicle car1 = Vehicle.builder().licensePlate("29A-12345").color("Trắng").type(VehicleType.CAR).build();
            Vehicle bike1 = Vehicle.builder().licensePlate("18B-99999").color("Đỏ").type(VehicleType.BIKE).build();
            Vehicle car2 = Vehicle.builder().licensePlate("30E-67890").color("Đen").type(VehicleType.CAR).build();
            vehicleRepository.saveAll(List.of(car1, bike1, car2));

            // 3. Tạo các vé gửi xe (ParkingTicket)
            ParkingTicket ticket1 = ParkingTicket.builder()
                    .vehicle(car1)
                    .zone(zoneA)
                    .checkInTime(LocalDateTime.now().minusHours(3))
                    .checkOutTime(null) // Đang trong bãi
                    .build();

            ParkingTicket ticket2 = ParkingTicket.builder()
                    .vehicle(bike1)
                    .zone(zoneB)
                    .checkInTime(LocalDateTime.now().minusHours(2))
                    .checkOutTime(null) // Đang trong bãi
                    .build();

            ParkingTicket ticket3 = ParkingTicket.builder()
                    .vehicle(car2)
                    .zone(zoneA)
                    .checkInTime(LocalDateTime.now().minusDays(1))
                    .checkOutTime(LocalDateTime.now().minusDays(1).plusHours(2)) // Đã check-out
                    .build();

            parkingTicketRepository.saveAll(List.of(ticket1, ticket2, ticket3));

            System.out.println("✅ [Ex1] Khởi tạo dữ liệu mẫu thành công cho hệ thống Bãi đỗ xe (PostgreSQL)!");
        }
    }
}
