#pragma once
//
// \author Oleksandr Tkachenko
// \email tkachenko@encrypto.cs.tu-darmstadt.de
// \organization Cryptography and Privacy Engineering Group (ENCRYPTO)
// \TU Darmstadt, Computer Science department
//
// \copyright The MIT License. Copyright Oleksandr Tkachenko
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the Software
// is furnished to do so, subject to the following conditions:
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
// OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

namespace ENCRYPTO {

struct PsiAnalyticsContext {
  uint16_t port;
  uint32_t role;
  uint64_t bitlen;
  uint64_t neles;
  uint64_t nbins;
  uint64_t notherpartyselems;
  uint64_t nthreads;
  uint64_t nfuns;  //< number of hash functions in the hash table
  uint64_t threshold;
  uint64_t polynomialsize;
  uint64_t polynomialbytelength;
  uint64_t nmegabins;
  double epsilon;
  std::string address;
  uint64_t payload_bitlen;


  enum {
    NONE,                 // only calculate the equality of the bin elements - used for benchmarking
    THRESHOLD,            // 1 if T > PSI, 0 otherwise
    SUM,                  // number of matched elements
    SUM_IF_GT_THRESHOLD,  // number of matched elements if T > PSI, 0 otherwise
    PAYLOAD_A_SUM,        // sum of payload values (sender parties) of matched elements
    PAYLOAD_A_SUM_GT,     // same as PAYLOAD_A_SUM but only if threshold is reached.
    PAYLOAD_AB_SUM,
    PAYLOAD_AB_SUM_GT,
    PAYLOAD_AB_MUL_SUM,
    PAYLOAD_AB_MUL_SUM_GT
  } analytics_type;
  
  uint64_t overlap;

  const uint64_t maxbitlen = 61;

  struct {
    double hashing;
    double base_ots_aby;
    double base_ots_libote;
    double oprf;
    double opprf;
    double polynomials;
    double polynomials_transmission;
    double aby_setup;
    double aby_online;
    double aby_total;
    double total;
  } timings;

  struct {
    uint64_t polynomials_transmission_recv;
    uint64_t polynomials_transmission_sent;
    uint64_t base_ots_libote_recv;
    uint64_t base_ots_libote_sent;
    uint64_t oprf_recv;
    uint64_t oprf_sent;
    uint64_t aby_setup_recv;
    uint64_t aby_online_recv;
    uint64_t base_ots_aby_recv;
    uint64_t aby_total_recv;
    uint64_t aby_setup_sent;
    uint64_t aby_online_sent;
    uint64_t base_ots_aby_sent;
    uint64_t aby_total_sent;
  } comm;
};

}