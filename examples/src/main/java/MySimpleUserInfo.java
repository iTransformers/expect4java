import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * Created by Administrator on 10/27/2015.
 */
public class MySimpleUserInfo implements UserInfo, UIKeyboardInteractive {
    protected String password;

    public MySimpleUserInfo(String password) {
        this.password = password;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean promptPassword(String s) {
        return true;
    }

    @Override
    public boolean promptPassphrase(String s) {
        return false;
    }

    @Override
    public boolean promptYesNo(String s) {
        return true;
    }

    @Override
    public void showMessage(String s) {
        System.out.println(s);
    }

    @Override
    public String[] promptKeyboardInteractive(String s, String s1, String s2, String[] strings, boolean[] booleans) {
        return new String[]{password};
    }
}
