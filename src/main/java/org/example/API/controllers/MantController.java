package org.example.API.controllers;

import com.google.gson.Gson;
import org.example.DataAccess.services.MantService;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.auth.UserResponseDto;
import org.example.Domain.dtos.cars.CarResponseDto;
import org.example.Domain.dtos.mantenimiento.*;
import org.example.Domain.models.Mantenimiento;

import java.util.List;
import java.util.stream.Collectors;

public class MantController {

    private final MantService mantService;
    private final Gson gson = new Gson();

    public MantController(MantService mantService) {
        this.mantService = mantService;
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "add":
                    return handleAddMant(request);
                case "update":
                    return handleUpdateMant(request);
                case "delete":
                    return handleDeleteMant(request);
                case "list":
                    return handleListMant(request);
                case "get":
                    return handleGetMant(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    // --- ADD CAR ---
    private ResponseDto handleAddMant(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            AddMantRequestDto dto = gson.fromJson(request.getData(), AddMantRequestDto.class);
            Mantenimiento mant = mantService.createMantenimiento(dto.getFecha(), dto.getDescripcion(), dto.getTipo(), dto.getCarId());

            MantResponseDto response = toResponseDto(mant);
            return new ResponseDto(true, "Mant added successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleAddMant: " + e.getMessage());
            throw e;
        }
    }

    // --- UPDATE CAR ---
    private ResponseDto handleUpdateMant(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            UpdateMantRequestDto dto = gson.fromJson(request.getData(), UpdateMantRequestDto.class);
            Mantenimiento updated = mantService.updateMantenimiento(dto.getId(), dto.getFecha(), dto.getDescripcion(), dto.getTipo());

            if (updated == null) {
                return new ResponseDto(false, "Car not found", null);
            }

            MantResponseDto response = toResponseDto(updated);
            return new ResponseDto(true, "Car updated successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleUpdateMant: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // --- DELETE CAR ---
    private ResponseDto handleDeleteMant(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            DeleteMantRequestDto dto = gson.fromJson(request.getData(), DeleteMantRequestDto.class);
            boolean deleted = mantService.deleteCar(dto.getId());

            if (!deleted) {
                return new ResponseDto(false, "Car not found or could not be deleted", null);
            }

            return new ResponseDto(true, "Car deleted successfully", null);
        } catch (Exception e) {
            System.out.println("Error in handleDeleteMant: " + e.getMessage());
            throw e;
        }
    }

    // --- LIST CARS ---
    private ResponseDto handleListMant(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            List<Mantenimiento> mants = mantService.getAllMantenimientos();
            List<MantResponseDto> mantDtos = mants.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListMantResponseDto response = new ListMantResponseDto(mantDtos);
            return new ResponseDto(true, "Maintanence retrieved successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleListMant: " + e.getMessage());
            throw e;
        }
    }

    // --- GET SINGLE CAR ---
    private ResponseDto handleGetMant(RequestDto request) {
        try {
            if (request.getToken() == null || request.getToken().isEmpty()) {
                return new ResponseDto(false, "Unauthorized", null);
            }

            DeleteMantRequestDto dto = gson.fromJson(request.getData(), DeleteMantRequestDto.class);
            Mantenimiento mantenimiento = mantService.getMantenimientoById(dto.getId());

            if (mantenimiento == null) {
                return new ResponseDto(false, "Mantineance not found", null);
            }

            MantResponseDto response = toResponseDto(mantenimiento);
            return new ResponseDto(true, "Car retrieved successfully", gson.toJson(response));
        } catch (Exception e) {
            System.out.println("Error in handleGetMant: " + e.getMessage());
            throw e;
        }
    }

    // --- Helper method ---
    private MantResponseDto toResponseDto(Mantenimiento mantenimiento) {
        var owner = mantenimiento.getCar().getOwner();
        var ownerDto = new UserResponseDto(
                owner.getId(),
                owner.getUsername(),
                owner.getEmail(),
                owner.getRole(),
                owner.getCreatedAt(),
                owner.getUpdatedAt()
        );

        var carDto = new CarResponseDto(
                mantenimiento.getCar().getId(),
                mantenimiento.getCar().getMake(),
                mantenimiento.getCar().getModel(),
                mantenimiento.getCar().getYear(),
                ownerDto, // <-- aquí se usa el UserResponseDto
                mantenimiento.getCar().getCreatedAt().toString(),
                mantenimiento.getCar().getUpdatedAt().toString()
        );

        return new MantResponseDto(
                mantenimiento.getId(),
                mantenimiento.getFecha(),
                mantenimiento.getDescripcion(),
                mantenimiento.getTipo(),
                carDto // <-- mejor incluir el carro completo, no solo el ID
        );
    }
}