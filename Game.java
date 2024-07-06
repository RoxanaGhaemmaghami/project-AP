import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// کلاس انتزاعی کارت
abstract class Card {
    private String name;

    public Card(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// کلاس‌های مختلف کارت‌ها
class CharacterCard extends Card {
    public CharacterCard(String name) {
        super(name);
    }
}

class LocationCard extends Card {
    public LocationCard(String name) {
        super(name);
    }
}

class RoomCard extends Card {
    public RoomCard(String name) {
        super(name);
    }
}

// کلاس بازیکن
class Player {
    private List<Card> hand;

    public Player() {
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }
}

// کلاس بازی
public class Game {
    private List<Card> characterCards;
    private List<Card> locationCards;
    private List<Card> roomCards;
    private List<Card> allCards;
    private Card mainCharacterCard;
    private Card mainLocationCard;
    private Card mainRoomCard;
    private List<Player> players;
    private Random random;
    private String finalRoom;
    private String finalLocation;
    private String finalCharacter;


    public Game() {
        characterCards = new ArrayList<>();
        locationCards = new ArrayList<>();
        roomCards = new ArrayList<>();
        allCards = new ArrayList<>();
        players = new ArrayList<>();
        random = new Random();
        // ایجاد کارت‌های شخصیت
        for (int i = 1; i <= 6; i++) {
            characterCards.add(new CharacterCard("شخصیت" + i));
        }

        // ایجاد کارت‌های محل
        String[] locations = {"زیر گلدان", "کشوی مخفی", "پشت عکس", "داخل جعبه", "زیر میز", "بالای کمد"};
        for (String location : locations) {
            locationCards.add(new LocationCard(location));
        }

        // ایجاد کارت‌های اتاق
        for (int i = 1; i <= 9; i++) {
            roomCards.add(new RoomCard("اتاق شماره" + i));
        }

        // مخلوط کردن کارت‌ها و انتخاب کارت‌های اصلی
        shuffleAndSelectMainCards();

        // ایجاد بازیکنان
        for (int i = 0; i < 3; i++) {
            players.add(new Player());
        }

        // توزیع کارت‌ها به بازیکنان
        distributeCards();
    }

    public void shuffleAndSelectMainCards() {
        Collections.shuffle(characterCards);
        Collections.shuffle(locationCards);
        Collections.shuffle(roomCards);

        mainCharacterCard = characterCards.remove(0);
        mainLocationCard = locationCards.remove(0);
        mainRoomCard = roomCards.remove(0);

        allCards.addAll(characterCards);
        allCards.addAll(locationCards);
        allCards.addAll(roomCards);
        Collections.shuffle(allCards);
    }

    private void distributeCards() {
        int playerIndex = 0;
        for (Card card : allCards) {
            players.get(playerIndex).addCard(card);
            playerIndex = (playerIndex + 1) % players.size();
        }
    }

    public void rollDiceAndChooseRoom() {
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;
        int sum = dice1 + dice2;

        System.out.println("تاس اول: " + dice1);
        System.out.println("تاس دوم: " + dice2);
        System.out.println("مجموع تاس‌ها: " + sum);

        Scanner scanner = new Scanner(System.in);
        System.out.println("در چه اتاقی می‌خواهید مستقر شوید؟");

        List<String> availableRooms = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if ((sum % 2 == 0 && i % 2 == 0) || (sum % 2 != 0 && i % 2 != 0)) {
                availableRooms.add("اتاق شماره" + i);
            }
        }

        System.out.println("اتاق‌های موجود: " + availableRooms);
        String chosenRoom = scanner.nextLine();

        if (availableRooms.contains(chosenRoom)) {
            finalRoom = chosenRoom;
            System.out.println("شما در " + chosenRoom + " مستقر شدید.");
        } else {
            System.out.println("اتاق انتخابی معتبر نیست. لطفاً یکی از اتاق‌های موجود را انتخاب کنید.");
            rollDiceAndChooseRoom(); // دوباره از کاربر می‌خواهد اتاق را انتخاب کند
            return;
        }

        System.out.println("حدس شما راجب محل چیست؟");
        finalLocation = scanner.nextLine();

        System.out.println("حدس شما راجب شخصیت چیست؟");
        finalCharacter = scanner.nextLine();

        checkPlayerCards();
        askFinalGuess(mainCharacterCard.getName(), mainLocationCard.getName(), mainRoomCard.getName());
    }

    private void checkPlayerCards() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            List<Card> hand = player.getHand();
            List<Card> matchingCards = new ArrayList<>();

            for (Card card : hand) {
                if (card.getName().equals(finalCharacter) || card.getName().equals(finalLocation) || card.getName().equals(finalRoom)) {
                    matchingCards.add(card);
                }
            }

            if (!matchingCards.isEmpty()) {
                Card cardToShow = matchingCards.get(random.nextInt(matchingCards.size()));
                System.out.println("بازیکن " + (i + 1) + " دارای کارت " + cardToShow.getName() + " است.");
            }
        }
    }

    public void askFinalGuess(String x, String y, String z) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("آیا حدس نهایی دارید؟ (بله/خیر)");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("بله")) {
            System.out.println("جواب نهایی را بگویید؟");
            System.out.println("شخصیت:");
            String finalCharacterGuess = scanner.nextLine();
            System.out.println("محل:");
            String finalLocationGuess = scanner.nextLine();
            System.out.println("اتاق:");
            String finalRoomGuess = scanner.nextLine();


            if (finalCharacterGuess.equals(x) && finalLocationGuess.equals(y) && finalRoomGuess.equals(z)) {
                System.out.println("تبریک میگم برنده شدین...!!!");
            } else {
                System.out.println("متاسفم بازی رو باختین...!!!");
            }
        } else {
            rollDiceAndChooseRoom(); // بازی به مرحله تاس ریختن برمی‌گردد
        }
    }

    public void printGameState() {
//        System.out.println("کارت اصلی شخصیت: " + mainCharacterCard.getName());
//        System.out.println("کارت اصلی محل: " + mainLocationCard.getName());
//        System.out.println("کارت اصلی اتاق: " + mainRoomCard.getName());

        for (int i = 0; i < players.size(); i++) {
            System.out.println("کارت‌های بازیکن " + (i + 1) + ":");
            for (Card card : players.get(i).getHand()) {
                System.out.println(card.getName());
            }
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.printGameState();
        game.rollDiceAndChooseRoom();
    }
}
