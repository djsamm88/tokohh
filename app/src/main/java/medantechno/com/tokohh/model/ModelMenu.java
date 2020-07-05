package medantechno.com.tokohh.model;

public class ModelMenu {
    public ModelMenu()
    {

    }

    String IdMenu,IdResto,Menu,Deskripsi,Harga,IsTersedia,IsAktif,FileGambar,IdPemilik,Nama,Alamat,Lat,Long,IsHalal,IsBuka;
    public ModelMenu(String IdMenu,String IdResto,String Menu,String Deskripsi,String Harga,String IsTersedia,String IsAktif,String FileGambar,String IdPemilik,String Nama,String Alamat,String Lat,String Long,String IsHalal,String IsBuka)
    {
        this.IdMenu=IdMenu;
        this.IdResto=IdResto;
        this.Menu=Menu;
        this.Deskripsi=Deskripsi;
        this.Harga=Harga;
        this.IsTersedia=IsTersedia;
        this.IsAktif=IsAktif;
        this.FileGambar=FileGambar;
        this.IdPemilik=IdPemilik;
        this.Nama=Nama;
        this.Alamat=Alamat;
        this.Lat=Lat;
        this.Long=Long;
        this.IsHalal=IsHalal;
        this.IsBuka=IsBuka;

    }

    public String getIdMenu() {
        return IdMenu;
    }

    public String getIdResto() {
        return IdResto;
    }

    public String getMenu() {
        return Menu;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public String getHarga() {
        return Harga;
    }

    public String getIsTersedia() {
        return IsTersedia;
    }

    public String getIsAktif() {
        return IsAktif;
    }

    public String getFileGambar() {
        return FileGambar;
    }

    public String getIdPemilik() {
        return IdPemilik;
    }

    public String getNama() {
        return Nama;
    }

    public String getAlamat() {
        return Alamat;
    }

    public String getLat() {
        return Lat;
    }

    public String getLong() {
        return Long;
    }

    public String getIsHalal() {
        return IsHalal;
    }

    public String getIsBuka() {
        return IsBuka;
    }

    public void setIdMenu(String idMenu) {
        IdMenu = idMenu;
    }

    public void setIdResto(String idResto) {
        IdResto = idResto;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

    public void setIsTersedia(String isTersedia) {
        IsTersedia = isTersedia;
    }

    public void setIsAktif(String isAktif) {
        IsAktif = isAktif;
    }

    public void setFileGambar(String fileGambar) {
        FileGambar = fileGambar;
    }

    public void setIdPemilik(String idPemilik) {
        IdPemilik = idPemilik;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public void setIsHalal(String isHalal) {
        IsHalal = isHalal;
    }

    public void setIsBuka(String isBuka) {
        IsBuka = isBuka;
    }
}
