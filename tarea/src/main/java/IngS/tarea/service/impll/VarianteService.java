package IngS.tarea.service.impll;

import IngS.tarea.model.Variante;
import IngS.tarea.repository.VarianteRep;
import IngS.tarea.service.IVarianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class VarianteService implements IVarianteService {

    @Autowired
    private VarianteRep varianteRepository;

    @Override
    public Variante crearVariante(Variante variante) {
        return varianteRepository.save(variante);
    }

    @Override
    public List<Variante> listarVariantes() {
        return varianteRepository.findAll();
    }

    @Override
    public Optional<Variante> buscarVariantePorId(Long id) {
        return varianteRepository.findById(id);
    }

    @Override
    public Optional<Variante> buscarVariantePorNombre(String nombre) {
        return varianteRepository.findByNombreVariante(nombre);
    }

    @Override
    public BigDecimal calcularPrecioConVariante(BigDecimal precioBase, Long varianteId) {
        if (varianteId == null) {
            return precioBase;
        }

        Optional<Variante> varianteOpt = varianteRepository.findById(varianteId);
        if (varianteOpt.isPresent()) {
            Variante variante = varianteOpt.get();
            return precioBase.add(variante.getPrecioAdicional());
        }

        return precioBase;
    }

    @Override
    public Variante obtenerVarianteNormal() {
        Optional<Variante> normalOpt = varianteRepository.findByNombreVariante("normal");

        if (normalOpt.isPresent()) {
            return normalOpt.get();
        }

        Variante normal = new Variante();
        normal.setNombreVariante("normal");
        normal.setPrecioAdicional(BigDecimal.ZERO);
        normal.setDescripcion("Sin variante adicional - precio base");

        return varianteRepository.save(normal);
    }

    @Override
    public Variante actualizarVariante(Long id, Variante variante) {
        Optional<Variante> varianteExistenteOpt = varianteRepository.findById(id);

        if (varianteExistenteOpt.isPresent()) {
            Variante varianteExistente = varianteExistenteOpt.get();

            if (variante.getNombreVariante() != null) {
                varianteExistente.setNombreVariante(variante.getNombreVariante());
            }
            if (variante.getPrecioAdicional() != null) {
                varianteExistente.setPrecioAdicional(variante.getPrecioAdicional());
            }
            if (variante.getDescripcion() != null) {
                varianteExistente.setDescripcion(variante.getDescripcion());
            }

            return varianteRepository.save(varianteExistente);
        }

        return null;
    }

    @Override
    public boolean eliminarVariante(Long id) {
        if (varianteRepository.existsById(id)) {
            varianteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
