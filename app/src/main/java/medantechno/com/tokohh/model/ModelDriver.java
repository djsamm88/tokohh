package medantechno.com.tokohh.model;

public class ModelDriver {
    public ModelDriver()
    {

    }
    String IdDriver,IdPengguna,Nama,NoSIM,NoPlat,IsAktif,no_hp,Foto;

    public ModelDriver(String IdDriver, String IdPengguna, String Nama, String NoSIM, String NoPlat,String IsAktif, String no_hp, String Foto)
    {
        this.IdDriver=IdDriver;
        this.IdPengguna=IdPengguna;
        this.Nama=Nama;
        this.NoSIM=NoSIM;
        this.NoPlat=NoPlat;
        this.IsAktif=IsAktif;
        this.no_hp=no_hp;
        this.Foto=Foto;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getIsAktif() {
        return IsAktif;
    }

    public void setIsAktif(String isAktif) {
        IsAktif = isAktif;
    }

    public String getNoPlat() {
        return NoPlat;
    }

    public void setNoPlat(String noPlat) {
        NoPlat = noPlat;
    }

    public String getNoSIM() {
        return NoSIM;
    }

    public void setNoSIM(String noSIM) {
        NoSIM = noSIM;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getIdPengguna() {
        return IdPengguna;
    }

    public void setIdPengguna(String idPengguna) {
        IdPengguna = idPengguna;
    }

    public String getIdDriver() {
        return IdDriver;
    }

    public void setIdDriver(String idDriver) {
        IdDriver = idDriver;
    }
}
