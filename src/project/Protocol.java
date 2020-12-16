package project;

public class Protocol {
   public static final String REGISTER = "100"; // ȸ������(request)

   public static final String IDSEARCH = "110"; // IDã�� Join (request)

   public static final String IDSEARCH_OK = "111"; // IDã�� (������ �ִ°�) (ACK)

   public static final String IDSEARCH_NO = "112"; // IDã�� (������ ����) (NACK)

   public static final String IDSEARCHCHECK = "113"; // (using ȸ������)IDã�� �ߺ�Ȯ��(request)

   public static final String IDSEARCHCHECK_OK = "114"; // (using ȸ������)ID �ߺ�Ȯ�� (��밡��) (ACK)

   public static final String IDSEARCHCHECK_NO = "115"; // (using ȸ������)ID �ߺ�Ȯ�� (��� �Ұ���) (NACK)
   
   public static final String NSEARCHCHECK = "116"; // (using ȸ������)�г��� ã�� �ߺ�Ȯ��(request)

   public static final String NSEARCHCHECK_OK = "117"; // (using ȸ������)�г��� �ߺ�Ȯ�� (��밡��) (ACK)

   public static final String NSEARCHCHECK_NO = "118"; // (using ȸ������)�г��� �ߺ�Ȯ�� (��� �Ұ���) (NACK)

   public static final String ENTERLOGIN = "120"; // �α���(request)

   public static final String ENTERLOGIN_OK = "121"; // �α��� ����(ACK)

   public static final String ENTERLOGIN_NO = "122"; // �α��� ����(NACK)

   public static final String PWSEARCH = "130"; // PWã��

   public static final String ROOMSORT = "210"; // ������

   public static final String EXITWAITROOM = "220"; // ���� ������(= logout)

   public static final String SENDWAITROOM = "250"; // ���� �޼���

   public static final String ENTERROOM = "300"; // ������

   public static final String ENTERROOM_OK = "301"; // ������ ����

   public static final String ENTERROOM_OK1 = "302"; // ������ ���� //�����ϴ� �����

   public static final String ENTERROOM_NO = "303"; // ������ ����

   public static final String ENTERROOM_USERLISTSEND = "305"; // �濡 �������� ������

   public static final String EXITCHATTINGROOM = "310"; // �泪���� (ä�ù� ������)

   public static final String SENDMESSAGE = "400"; // �޼��� ������

   public static final String SENDMESSAGE_ACK = "410"; // �޼��� ������(����)

   public static final String CHATTINGSENDMESSAGE = "420"; // ä�ù濡�� �޼��� ������ (Request)

   public static final String CHATTINGSENDMESSAGE_OK = "430"; // ä�ù濡�� �޼��� ������ (Request)

   public static final String CHATTINGFILESEND_SYN = "500"; // ��������1

   public static final String CHATTINGFILESEND_SYNACK = "510"; // ��������2

   public static final String CHATTINGFILESEND_FILE = "520"; // ��������3

   public static final String CHATTINGFILESEND_FILEACK = "530"; // ��������4

   public static final String CHATTINGFILEDOWNLOAD_SYN = "550"; // ���� �ٿ�ε�1

   public static final String CHATTINGFILEDOWNLOAD_SEND = "560"; // ���� ����
   
   public static final String EXIT = "565"; // �α׾ƿ�
   
   public static final String CHATEXIT = "566"; // �泪���� (ä�ù� ������)
   
   public static final String ADDFRIEND = "7979";//ģ���߰�
   
   public static final String SEARCHFRIEND = "7917";//ģ��ã��
   
   public static final String TODAYMSG = "570";// ������ �� ���� �߰�
   
   public static final String FRIENDLOGIN = "580";
   
   public static final String SEARCHFRIEND_NO = "7920";
   
   public static final String VIEWFRIENDPROFILE = "7930";//ģ��ã��
   
   public static final String CHATREQUEST = "200";
   
   public static final String CHATREQUEST_OK = "201";
   
   public static final String CHATREQUEST_NO = "202";
   
   public static final String DELETEMEMBER = "8000"; // ȸ�� Ż��

}
