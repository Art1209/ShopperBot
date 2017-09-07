package ShopperBot;

public class ShopFlow {
    private Shop shop;
    public boolean mainFlow(){
        if (!shop.checkJoinedAccount())shop.login();
        return false;
    }
    private enum Shop{
        Bang{
            @Override
            boolean login() {
                return false;
            }

            @Override
            boolean checkJoinedAccount() {
                return false;
            }
        },
        Gear{
            @Override
            boolean login() {
                return false;
            }

            @Override
            boolean checkJoinedAccount() {
                return false;
            }
        };
        abstract boolean  login();
        abstract boolean checkJoinedAccount();
        private String loginLink;
    }

}
