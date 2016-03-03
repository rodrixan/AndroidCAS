package es.uam.eps.tfg.cas.android.examples.criminalintent.model.services;


import java.util.List;
import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;

public interface CrimeLab {


    public List<Crime> getCrimes();

    public Crime getCrime(UUID crimeId);
}
