import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.io.*;
import java.util.Random;

public class TimeTable extends JFrame implements ActionListener {

	private JPanel screen = new JPanel(), tools = new JPanel();
	private JButton tool[];
	private JTextField field[];
	private CourseArray courses;
	private Color CRScolor[] = {Color.RED, Color.GREEN, Color.BLACK};
	private Autoassociator autoassociator;
	
	public TimeTable() {
		super("Dynamic Time Table");
		setSize(500, 800);
		setLayout(new FlowLayout());
		
		screen.setPreferredSize(new Dimension(400, 800));
		add(screen);
		
		setTools();
		add(tools);
		
		setVisible(true);
	}
	
	public void setTools() {
		String capField[] = {"Slots:", "Courses:", "Clash File:", "Iters:", "Shift:", "Cycles:"};
		field = new JTextField[capField.length];
		
		String capButton[] = {"Load", "Start", "Step", "Print", "WLog", "uUpd", "Train", "Cont", "Exit"};
		tool = new JButton[capButton.length];
		
		tools.setLayout(new GridLayout(2 * capField.length + capButton.length, 1));
		
		for (int i = 0; i < field.length; i++) {
			tools.add(new JLabel(capField[i]));
			field[i] = new JTextField(5);
			tools.add(field[i]);
		}
		
		for (int i = 0; i < tool.length; i++) {
			tool[i] = new JButton(capButton[i]);
			tool[i].addActionListener(this);
			tools.add(tool[i]);
		}
		
		field[0].setText("30");
		field[1].setText("622");
		field[2].setText("./src/uta-s-92.stu");
		field[3].setText("1");
		field[5].setText("15");
	}
	
	public void draw() {
		Graphics g = screen.getGraphics();
		int width = Integer.parseInt(field[0].getText()) * 10;
		for (int courseIndex = 1; courseIndex < courses.length(); courseIndex++) {
			g.setColor(CRScolor[courses.status(courseIndex) > 0 ? 0 : 1]);
			g.drawLine(0, courseIndex, width, courseIndex);
			g.setColor(CRScolor[CRScolor.length - 1]);
			g.drawLine(10 * courses.slot(courseIndex), courseIndex, 10 * courses.slot(courseIndex) + 10, courseIndex);
		}
	}
	
	private int getButtonIndex(JButton source) {
		int result = 0;
		while (source != tool[result]) result++;
		return result;
	}
	
	public void actionPerformed(ActionEvent click) {
		int min, step, clashes;
		String filename = "./src/uta-s-92_30_slots_0_clash.txt";
		switch (getButtonIndex((JButton) click.getSource())) {
		case 0:
			int slots = Integer.parseInt(field[0].getText());
			courses = new CourseArray(Integer.parseInt(field[1].getText()) + 1, slots);
			courses.readClashes(field[2].getText());
			draw();
			autoassociator = new Autoassociator(courses);
			break;
		case 1:
			min = Integer.MAX_VALUE;
			step = 0;
			for (int i = 1; i < courses.length(); i++) courses.setSlot(i, 0);

			for (int iteration = 1; iteration <= Integer.parseInt(field[3].getText()); iteration++) {
				courses.iterate(Integer.parseInt(field[4].getText()));
				draw();
				clashes = courses.clashesLeft();
				if (clashes < min) {
					min = clashes;
					step = iteration;
				}
			}
			System.out.println("Shift = " + field[4].getText() + "\tMin clashes = " + min + "\tat step " + step);
			setVisible(true);
			break;
		case 2:
			courses.iterate(Integer.parseInt(field[4].getText()));
			draw();
			break;
		case 3:
			System.out.println("Exam\tSlot\tClashes");
			for (int i = 1; i < courses.length(); i++)
				System.out.println(i + "\t" + courses.slot(i) + "\t" + courses.status(i));
			break;
		case 4:
			System.out.println("Exam\tSlot\tClashes");
			for (int i = 0; i < Integer.parseInt(field[0].getText()); i++) {
				System.out.println(Arrays.toString(courses.slotStatus(i)));
				if(courses.slotStatus(i)[0] > 10 && courses.slotStatus(i)[1] == 0) {
					System.out.println("I wish to be like a Slot:" + i);
					System.out.println(Arrays.toString(courses.getTimeSlot(i)));
					System.out.println(Arrays.toString(courses.slotStatus(i)));
					try {
						File file = new File(filename);
						boolean lineExists = false;
						BufferedReader reader = new BufferedReader(new FileReader(file));
						String line;
						while ((line = reader.readLine()) != null) {
							if (line.equals("Index: " + i + " Timeslot: " + Arrays.toString(courses.getTimeSlot(i)))) {
								lineExists = true;
								break;
							}
						}
						reader.close();
						if (!lineExists) {
							FileWriter writer = new FileWriter(file,true);
							BufferedWriter bufferedWriter = new BufferedWriter(writer);
							String newLine = "Index: " + i + " Timeslot: " + Arrays.toString(courses.getTimeSlot(i)) + "\n";
							bufferedWriter.write(newLine);
							bufferedWriter.close();
						} else {
							System.out.println("Line already exists in the file.");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			break;
		case 5:
			int cycles = Integer.parseInt(field[5].getText());
			step = 0;
			min = Integer.MAX_VALUE;
			for (int i = 0; i < cycles; i++) {
				for (int iteration = 1; iteration <= Integer.parseInt(field[3].getText()); iteration++) {
					courses.iterate(Integer.parseInt(field[4].getText()));
					Random random = new Random();
					int randomSlot = random.nextInt(Integer.parseInt(field[0].getText()));
					int index = autoassociator.unitUpdate(courses.getTimeSlot(randomSlot));
					courses.setSlot(index+1, randomSlot);
					clashes = courses.clashesLeft();
					if (clashes < min) {
						min = clashes;
						step = iteration;
					}
				}
				System.out.println("Shift = " + field[4].getText() + "\tMin clashes = " + min + "\tat step " + step + "\tCycle = " + (i+1));
				draw();
			}
			break;
		case 6:
			List<int[]> timeslots = readTimeslotsFromFile(filename);
			timeslots = readTimeslotsFromFile(filename);
			for (int[] timeslotwithindex : timeslots) {
				int[] timeslot = Arrays.copyOfRange(timeslotwithindex, 1, timeslotwithindex.length);
				autoassociator.training(timeslot);
			}
			break;
		case 7:
			step = 0;
			min = Integer.MAX_VALUE;

			for (int iteration = 1; iteration <= Integer.parseInt(field[3].getText()); iteration++) {
				courses.iterate(Integer.parseInt(field[4].getText()));
				draw();
				clashes = courses.clashesLeft();
				if (clashes < min) {
					min = clashes;
					step = iteration;
				}
			}
			System.out.println("Shift = " + field[4].getText() + "\tMin clashes = " + min + "\tat step " + step);
			setVisible(true);
			break;
		case 8:
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		new TimeTable();
	}
	private List<int[]> readTimeslotsFromFile(String filename) {
		List<int[]> timeslots = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" Timeslot: ");
				if (parts.length == 2) {
					int index = Integer.parseInt(parts[0].replaceAll("Index: ", "").trim());
					String[] timeslotValues = parts[1].replaceAll("\\[|\\]", "").split(", ");
					int[] timeSlotInfo = new int[timeslotValues.length + 1];
					timeSlotInfo[0] = index;
					for (int i = 0; i < timeslotValues.length; i++) {
						timeSlotInfo[i + 1] = Integer.parseInt(timeslotValues[i]);
					}
					timeslots.add(timeSlotInfo);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return timeslots;
	}

}
