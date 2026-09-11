import { fireEvent, render, screen, waitFor , act} from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Home from "../components/Home";
import App from "../App";
import Navbar from "../components/NavBar";
import Footer from "../components/Footer";
import DisplayBikes from "../components/DisplayBikes";
import ApplyForm from "../components/ApplyForm";


test("renders_home_component_with_title_and_description", () => {
  render(<App />);

  // Check if the title and description are rendered
  const titleElement = screen.getByText("Welcome to Faster Bike Taxi");
  const descriptionElement = screen.getByText(
    "Apply now to become a bike taxi driver and start earning!"
  );

  expect(titleElement).toBeInTheDocument();
  expect(descriptionElement).toBeInTheDocument();
});

test("renders_apply_now_button_with_link_to_apply", () => {
  render(
    <MemoryRouter>
      <Home />
    </MemoryRouter>
  );

  // Check if the "Apply Now" button is rendered with the correct link
  const applyButton = screen.getByText("Apply Now");

  expect(applyButton).toBeInTheDocument();
  expect(applyButton).toHaveAttribute("href", "/apply");
});

test("renders_navbar_component_with_links", () => {
  render(<App />);

  // Check if the component renders the title and links
  const titleElement = screen.getByText("Faster Bike Taxi");
  const homeLink = screen.getByText("Home");
  const bikeDetailsLink = screen.getByText("Bike Details");

  expect(titleElement).toBeInTheDocument();
  expect(homeLink).toBeInTheDocument();
  expect(bikeDetailsLink).toBeInTheDocument();
});

test("checks_link_destinations", () => {
  render(
    <MemoryRouter>
      <Navbar />
    </MemoryRouter>
  );

  // Check if the links have the correct destinations
  const homeLink = screen.getByText("Home");
  const bikeDetailsLink = screen.getByText("Bike Details");

  expect(homeLink).toHaveAttribute("href", "/");
  expect(bikeDetailsLink).toHaveAttribute("href", "/bikedetails");
});

test("renders_footer_component_with_copyright_text", () => {
  render(<Footer />);

  // Check if the copyright text is rendered
  const copyrightText = screen.getByText(
    /Faster Bike Taxi. All rights reserved./i
  );

  expect(copyrightText).toBeInTheDocument();
});

test("fetching_and_displaying_bike_applications", async () => {
  // Mocked data to simulate the response from the API
  const MOCK_DATA = [
    {
      name: "John Doe",
      bikeNumber: "B123",
      age: 30,
      phoneNumber: "1234567890",
    },
    {
      name: "Alice Smith",
      bikeNumber: "B456",
      age: 28,
      phoneNumber: "9876543210",
    },
  ];

  // Mock the fetch function to return the mocked data
  const fetchMock = jest.spyOn(global, "fetch").mockResolvedValue({
    ok: true,
    json: () => Promise.resolve(MOCK_DATA),
  });

  render(<DisplayBikes />);

  await new Promise((resolve) => setTimeout(resolve, 2000));

  // Check if each song is displayed in the table

  await waitFor(() => {
  MOCK_DATA.forEach((application) => {
    expect(screen.getByText(application.name)).toBeInTheDocument();
    expect(screen.getByText(application.bikeNumber)).toBeInTheDocument();
    expect(screen.getByText(application.age)).toBeInTheDocument();
    expect(screen.getByText(application.phoneNumber)).toBeInTheDocument();
  });
});

  // Validate the fetch function call
  expect(fetchMock).toHaveBeenCalledWith(
    expect.stringContaining("/getAllBiketaxi"),
    expect.objectContaining({
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
  );
  fetchMock.mockRestore();
});


test("submits_valid_application_form", async () => {
  render(<ApplyForm />);

  // Fill in the form
  const nameInput = screen.getByLabelText("Name:");
  const bikenumberInput = screen.getByLabelText("Bike Number:");
  const ageInput = screen.getByLabelText("Age:");
  const phonenumberInput = screen.getByLabelText("Phone Number:");
  const submitButton = screen.getByText("Submit Application");

  await act(async () => {
  fireEvent.change(nameInput, { target: { value: "John Doe" } });
  fireEvent.change(bikenumberInput, { target: { value: "B123" } });
  fireEvent.change(ageInput, { target: { value: "25" } });
  fireEvent.change(phonenumberInput, { target: { value: "1234567890" } });

  })
  // Ensure that the form elements have the expected values
  expect(nameInput).toHaveValue("John Doe");
  expect(bikenumberInput).toHaveValue("B123");
  expect(ageInput).toHaveValue(Number(25));
  expect(phonenumberInput).toHaveValue("1234567890");
  const fetchMock = jest.spyOn(global, "fetch").mockResolvedValue({ ok: true });

  // Submit the form
  await act(async () => {
  fireEvent.click(submitButton);
  })
  expect(fetchMock).toHaveBeenCalledTimes(1);

  expect(fetchMock).toHaveBeenCalledWith(
    expect.stringContaining("/addBiketaxi"),
    expect.objectContaining({
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: expect.any(String),
    })
  );
  const button = screen.getByText("Submit Application");
  expect(button).toBeInTheDocument();
  fireEvent.click(button);
  await new Promise((resolve) => setTimeout(resolve, 2000));

  const bikeDetailsLink = screen.getByText("Your application has been submitted successfully!");
  expect(bikeDetailsLink).toBeInTheDocument();

  fetchMock.mockRestore();
});

test("submits_invalid_application_form", () => {
  render(<ApplyForm />);

  // Fill in the form with invalid data
  const submitButton = screen.getByText("Submit Application");
  fireEvent.click(submitButton); // Try to submit without filling in the form

  // Check for validation error messages
  expect(screen.getByText("Name is required")).toBeInTheDocument();
  expect(screen.getByText("Bike Number is required")).toBeInTheDocument();
  expect(screen.getByText("Age is required")).toBeInTheDocument();
  expect(screen.getByText("Phone Number is required")).toBeInTheDocument();
});

test("checks_all_components_and_routes", () => {
  
  render(<App />);
  const Home = screen.getByText(/Home/i);
  fireEvent.click(Home);
  expect(screen.getByText("Welcome to Faster Bike Taxi")).toBeInTheDocument();
  const applyLink = screen.getByText("Apply Now");
  expect(applyLink).toBeInTheDocument();
  fireEvent.click(applyLink);
  expect(screen.getByText("Apply to Join")).toBeInTheDocument();

  const bikeDetailsLink = screen.getByText("Bike Details");
  expect(bikeDetailsLink).toBeInTheDocument();
  fireEvent.click(bikeDetailsLink);
  expect(screen.getByText("Submitted Applications")).toBeInTheDocument();
});