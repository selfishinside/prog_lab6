package system;

import Collections.Vehicle;
import Generators.VehicleAsker;
import exceptions.NoArgumentException;
import exceptions.WrongArgumentException;
import managers.ExecuteScriptCommand;
import managers.HistoryCommand;


import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;


public class Server {
    private static SocketChannel socket;

    private static Deque<String> lastTwelweCommands = new LinkedList<>();

    public void initialize(String host, int port) throws IOException {
        try {
            InetSocketAddress address = new InetSocketAddress(host, port); // создаем адрес сокета (IP-адрес и порт)
            socket = SocketChannel.open();
            socket.connect(address);
            socket.configureBlocking(false); // неблокирующий режим ввода-вывода, когда идет ввод вывод паралельно идет другие операции
        } catch (RuntimeException | ConnectException e) {
            System.out.println("Server " + host + " on port " + port + " is not available");
            System.exit(1);
        }
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Welcome to app!");
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                if (command.equals("exit")) {
                    System.exit(1);
                }
                if (!command.isEmpty()) {
                    synchronized (lastTwelweCommands) {
                        if (lastTwelweCommands.size() >= 12) {
                            lastTwelweCommands.removeLast();
                        }
                        lastTwelweCommands.addFirst(command);
                    }
                Vehicle vehicle = new Vehicle();
                String key = null;
                boolean isClientCommand = false;
                if (command.contains("removeLower") || command.contains("removeById") || command.contains("countByFuelType") || command.contains("countLessThenFuelType")) {
                    if (command.split(" ").length == 2) {
                        key = command.split(" ")[1];
                    }
                }else if (command.equals("add") || command.equals("addIfMax")) {
                    vehicle = VehicleAsker.createVehicle();
                }else if (command.split(" ")[0].equals("executeScript")) {
                    ExecuteScriptCommand.execute(command);
                    isClientCommand = true;
                }else if (command.split(" ")[0].equals("updateId")) {
                    if (command.split(" ").length == 2) {
                        key = command.split(" ")[1];
                    }
                    vehicle = VehicleAsker.createVehicle();
                } else if (command.split(" ")[0].equals("history")) {
                    if (command.split(" ").length == 1) {
                        HistoryCommand.execute(lastTwelweCommands);
                        isClientCommand = true;
                    }
                }
                if (!isClientCommand) {
                    Request request = new Request(command, vehicle, key);
                    sendRequest(request);
                }
            }
        }
        }catch (IOException e) {
            System.out.println("Server is not availdable");
            System.out.println(e.getMessage() + " " + e);
        }catch (WrongArgumentException | NoArgumentException e) {
            System.out.println(e.getMessage());
        }catch (NullPointerException e) {
            System.out.println("");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendRequest(Request request) throws IOException {
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.close();
        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

        // Отправляем данные
        while (buffer.hasRemaining()) {
            socket.write(buffer);
        }

        try {
            Request request_server = getAnswer();
            System.out.println("Server answer: \n" + request_server.getMessage());
        } catch (ClassNotFoundException e) {
            // Обработка исключения, если класс Request не найден
            System.out.println("Wrong answer from server");
        } catch (IOException e) {
            // Обработка исключения ввода-вывода при чтении объекта
            System.out.println("Something wrong while reading answer from server");
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Request getAnswer() throws IOException, InterruptedException, ClassNotFoundException {
        Thread.sleep(2000);
        ArrayList<ByteBuffer> bufferList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            socket.read(buffer);
            if (buffer.limit() == buffer.position() || bufferList.isEmpty()) {
                bufferList.add(buffer);
            } else {
                break;
            }
        }
        ByteBuffer bigBuffer = ByteBuffer.allocate(bufferList.size() * 8192);
        for (ByteBuffer byteBuffer : bufferList) {
            bigBuffer.put(byteBuffer.array());
        }

        ByteArrayInputStream bi = new ByteArrayInputStream(bigBuffer.array());
        ObjectInputStream oi = new ObjectInputStream(bi);

        return (Request) oi.readObject();
    }
}
