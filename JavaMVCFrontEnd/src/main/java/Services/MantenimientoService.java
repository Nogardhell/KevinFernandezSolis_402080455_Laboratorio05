package Services;

import Domain.Dtos.RequestDto;
import Domain.Dtos.ResponseDto;
import Domain.Dtos.mantenimiento.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MantenimientoService extends BaseService {

    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public MantenimientoService(String host, int port) {
        super(host, port);
    }

    public Future<MantResponseDto> addMantAsync(AddMantRequestDto dto, Long userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Mantenimiento", "add", gson.toJson(dto), userId.toString());
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MantResponseDto.class);
        });
    }

    public Future<MantResponseDto> updateMantAsync(UpdateMantRequestDto dto, Long userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Mantenimiento", "update", gson.toJson(dto), userId.toString());
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MantResponseDto.class);
        });
    }

    public Future<Boolean> deleteMantAsync(DeleteMantRequestDto dto, Long userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Mantenimiento", "delete", gson.toJson(dto), userId.toString());
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    public Future<List<MantResponseDto>> listMantAsync(Long userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Mantenimiento", "list", "", userId.toString());
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListMantResponseDto listResponse = gson.fromJson(response.getData(), ListMantResponseDto.class);
            return listResponse.getMantenimientos();
        });
    }
}
