
class Transaction {
    private static final String transaction_file = "transaction_data.csv";

    public void bondPurchase(Bond bond) {
        try {
            FileReader fileName = new FileReader(transaction_file);
            BufferedReader br = new BufferedReader(fileName);
        }catch(IOException ie){
            System.out.println(ie);
        }
    }

    public void bondSelling(Bond bond){

    }
}