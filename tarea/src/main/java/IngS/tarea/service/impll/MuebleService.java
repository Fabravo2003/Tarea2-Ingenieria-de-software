package IngS.tarea.service.impll;

import IngS.tarea.model.Mueble;
import IngS.tarea.repository.MuebleRep;
import IngS.tarea.service.IMuebleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MuebleService implements IMuebleService {

    @Autowired
    private MuebleRep muebleRepository;

    @Override
    public Mueble crearMueble(Mueble mueble) {
        if (mueble.getEstado() == null || mueble.getEstado().isEmpty()) {
            mueble.setEstado("activo");
        }
        return muebleRepository.save(mueble);
    }

    @Override
    public List<Mueble> listarMuebles() {
        return muebleRepository.findAll();
    }

    @Override
    public List<Mueble> listarMueblesActivos() {
        return muebleRepository.findByEstado("activo");
    }

    @Override
    public Optional<Mueble> buscarMueblePorId(Long id) {
        return muebleRepository.findById(id);
    }

    @Override
    public Mueble actualizarMueble(Long id, Mueble mueble) {
        Optional<Mueble> muebleExistenteOpt = muebleRepository.findById(id);

        if (muebleExistenteOpt.isPresent()) {
            Mueble muebleExistente = muebleExistenteOpt.get();

            if (mueble.getNombreMueble() != null) {
                muebleExistente.setNombreMueble(mueble.getNombreMueble());
            }
            if (mueble.getTipo() != null) {
                muebleExistente.setTipo(mueble.getTipo());
            }
            if (mueble.getPrecioBase() != null) {
                muebleExistente.setPrecioBase(mueble.getPrecioBase());
            }
            if (mueble.getStock() != null) {
                muebleExistente.setStock(mueble.getStock());
            }
            if (mueble.getEstado() != null) {
                muebleExistente.setEstado(mueble.getEstado());
            }
            if (mueble.getTamanio() != null) {
                muebleExistente.setTamanio(mueble.getTamanio());
            }
            if (mueble.getMaterial() != null) {
                muebleExistente.setMaterial(mueble.getMaterial());
            }

            return muebleRepository.save(muebleExistente);
        }

        return null;
    }

    @Override
    public boolean desactivarMueble(Long id) {
        Optional<Mueble> muebleOpt = muebleRepository.findById(id);

        if (muebleOpt.isPresent()) {
            Mueble mueble = muebleOpt.get();
            mueble.setEstado("inactivo");
            muebleRepository.save(mueble);
            return true;
        }

        return false;
    }

    @Override
    public List<Mueble> buscarPorTipo(String tipo) {
        return muebleRepository.findByTipo(tipo);
    }

    @Override
    public boolean verificarStockDisponible(Long id, Integer cantidad) {
        Optional<Mueble> muebleOpt = muebleRepository.findById(id);

        if (muebleOpt.isPresent()) {
            Mueble mueble = muebleOpt.get();
            return mueble.getStock() >= cantidad;
        }

        return false;
    }

    @Override
    public Mueble actualizarStock(Long id, Integer cantidad) {
        Optional<Mueble> muebleOpt = muebleRepository.findById(id);

        if (muebleOpt.isPresent()) {
            Mueble mueble = muebleOpt.get();
            int nuevoStock = mueble.getStock() + cantidad;

            if (nuevoStock < 0) {
                nuevoStock = 0;
            }

            mueble.setStock(nuevoStock);
            return muebleRepository.save(mueble);
        }

        return null;
    }
}
