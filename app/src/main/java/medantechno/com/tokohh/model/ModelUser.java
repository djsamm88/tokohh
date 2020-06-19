package medantechno.com.tokohh.model;

/**
 * Created by dinaskominfokab.pakpakbharat on 25/09/18.
 */

public class ModelUser {

    private int id_user;
    private String UserID,Password,RoleID,LasLogin,IsSuspend,Email,Nama,NIK,NoTelp,Alamat,Foto,FotoKTP,FotoKTP2,TanggalRegistrasi,IsEmailVerified;


    public ModelUser(){

    }

    public ModelUser(int id_user,String UserID,String Password,String RoleID, String LasLogin,String IsSuspend,String Email, String Nama,String NIK,String NoTelp, String Alamat,String  Foto,String FotoKTP, String FotoKTP2, String TanggalRegistrasi, String IsEmailVerified)
    {
        this.id_user=id_user;
        this.UserID=UserID;
        this.Password=Password;
        this.RoleID=RoleID;
        this.LasLogin=LasLogin;
        this.IsSuspend=IsSuspend;
        this.Email=Email;
        this.Nama=Nama;
        this.NIK=NIK;
        this.NoTelp=NoTelp;
        this.Alamat=Alamat;
        this.Foto=Foto;
        this.FotoKTP=FotoKTP;
        this.FotoKTP2=FotoKTP2;
        this.TanggalRegistrasi=TanggalRegistrasi;
        this.IsEmailVerified=IsEmailVerified;

    }

    public int getId_user() {
        return id_user;
    }

    public String getUserName() {
        return UserID;
    }

    public String getPassword() {
        return Password;
    }

    public String getRoleID() {
        return RoleID;
    }

    public String getLasLogin() {
        return LasLogin;
    }

    public String getIsSuspend() {
        return IsSuspend;
    }

    public String getEmail() {
        return Email;
    }

    public String getNama() {
        return Nama;
    }

    public String getNIK() {
        return NIK;
    }

    public String getNoTelp() {
        return NoTelp;
    }

    public String getAlamat() {
        return Alamat;
    }

    public String getFoto() {
        return Foto;
    }

    public String getFotoKTP() {
        return FotoKTP;
    }

    public String getFotoKTP2() {
        return FotoKTP2;
    }

    public String getTanggalRegistrasi() {
        return TanggalRegistrasi;
    }

    public String getIsEmailVerified() {
        return IsEmailVerified;
    }
}
