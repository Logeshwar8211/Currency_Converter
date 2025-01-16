import { useState, useEffect } from "react";
import Select from "react-select";
import axios from "axios";
import "../App.css"; // Custom styles for better appearance

const CurrencyConnverter = () => {
  const [amount, setAmount] = useState(1);
  const [fromCurrency, setFromCurrency] = useState([]);
  const [toCurrency, setToCurrency] = useState([]);
  const [exchangeRate, setExchangeRate] = useState(null);
  const [countries, setCountries] = useState([]);

  // Fetch countries on mount
  useEffect(() => {
    axios.get("https://restcountries.com/v3.1/all").then((response) => {
      const options = response.data.map((country) => ({
        id: country.cca2,
        value: country.currencies ? Object.keys(country.currencies)[0] : null,
        label: (
          <div style={{ display: "flex", alignItems: "center" }}>
            <img
              src={country.flags.svg}
              alt={country.name.common}
              style={{ width: 20, height: 15, marginRight: 10 }}
            />
            {country.currencies
              ? `${Object.keys(country.currencies)[0]} - ${country.name.common}`
              : "N/A"}
          </div>
        ),
        symbol: country.currencies
          ? Object.values(country.currencies)[0].symbol
          : "N/A",
      }));
      setCountries(options);
      setFromCurrency(options.find((opt) => opt.id === "IN"));
      setToCurrency(options.find((opt) => opt.id === "US"));
    });
  }, []);

  // Fetch exchange rate
  const fetchExchangeRate = () => {
    if (!fromCurrency || !toCurrency) return;
    axios
      .post("http://localhost:8080/api/convert", {
        from: fromCurrency.value,
        to: toCurrency.value,
        amount,
      })
      .then((response) => {
        setExchangeRate(response.data.Rate || 0);
        console.log(response.data);
      });
  };
  useEffect(() => {
    if (fromCurrency?.value && toCurrency?.value) {
      fetchExchangeRate();
    }
  }, [fromCurrency, toCurrency]);

  const handleSwap = () => {
    const temp = fromCurrency;
    setFromCurrency(toCurrency);
    setToCurrency(temp);
  };

  return (
    <div className="container bg-light  mt-5 rounded">
      <h1 className="text-center mt-4 text-primary">Currency Converter</h1>
      <p className="text-center text-dark">Get real-time exchange rates</p>
      <div className="converter-box">
        {/* Amount Input */}
        <div className="mb-3 ">
          <label htmlFor="amount" className="form-label mr-20">
            Amount
          </label>
          <input
            type="number"
            id="amount"
            className="form-control"
            value={amount}
            onChange={(e) => setAmount(parseInt(e.target.value))}
            min="1"
          />
        </div>
        {/* From Currency */}
        <div className="dropdown-container">
          <div className="currency-dropdown">
            <label>From</label>
            <Select
              options={countries}
              value={fromCurrency}
              onChange={setFromCurrency}
              placeholder="Select currency"
            />
          </div>

          {/* Swap Button */}
          <button
            className="swap-btn mt-4"
            onClick={handleSwap}
            aria-label="Swap currencies"
          >
            ⇄
          </button>

          {/* To Currency */}
          <div className="currency-dropdown">
            <label>To</label>
            <Select
              options={countries}
              value={toCurrency}
              onChange={setToCurrency}
              placeholder="Select currency"
            />
          </div>
        </div>

        {/* Exchange Rate Display */}
        <div className="exchange-rate mt-4">
          {fromCurrency && toCurrency && exchangeRate !== null ? (
            <>
              <p className="text-muted">
                {`1 ${fromCurrency.value} = ${exchangeRate.toFixed(4)} ${
                  toCurrency.value
                }`}
              </p>
              <h3>{`${amount} ${fromCurrency.value} = ${(
                amount * exchangeRate
              ).toFixed(2)} ${toCurrency.value}`}</h3>
            </>
          ) : (
            <p>Loading exchange rate...</p>
          )}
        </div>

        {/* Refresh Button */}
        <button className="btn btn-primary mt-3" onClick={fetchExchangeRate}>
          Refresh Rate
        </button>
      </div>
    </div>
  );
};

export default CurrencyConnverter;
