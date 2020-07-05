package medantechno.com.tokohh.model;

public class ModelPesanan {

    Integer IdMenu;
    String Harga,Jumlah,Subtotal,Catatan,IdResto,FileGambar,Menu;
    public ModelPesanan(){

    }

    public ModelPesanan(Integer IdMenu, String Harga,String Jumlah,String Subtotal,String Catatan,String IdResto,String FileGambar,String Menu)
    {
        this.IdMenu=IdMenu;
        this.Harga=Harga;
        this.Jumlah=Jumlah;
        this.Subtotal=Subtotal;
        this.Catatan=Catatan;
        this.IdResto=IdResto;
        this.FileGambar=FileGambar;
        this.Menu=Menu;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public String getFileGambar() {
        return FileGambar;
    }

    public void setFileGambar(String fileGambar) {
        FileGambar = fileGambar;
    }

    public String getIdResto() {
        return IdResto;
    }

    public void setIdResto(String idResto) {
        IdResto = idResto;
    }

    public String getCatatan() {
        return Catatan;
    }

    public void setCatatan(String catatan) {
        Catatan = catatan;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(String subtotal) {
        Subtotal = subtotal;
    }

    public String getJumlah() {
        return Jumlah;
    }

    public void setJumlah(String jumlah) {
        Jumlah = jumlah;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String harga) {
        Harga = harga;
    }

    public int getIdMenu() {
        return IdMenu;
    }

    public void setIdMenu(int idMenu) {
        IdMenu = idMenu;
    }
}
