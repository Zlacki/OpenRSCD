#ifndef _RSC_HPP
#define _RSC_HPP

#include <string>
#include <vector>
#include <boost/asio.hpp>
#include <boost/noncopyable.hpp>
#include <boost/shared_ptr.hpp>
#include "io/io_service_pool.hpp"
#include "io/connection.hpp"

namespace RSC {
	using namespace IO;

	class Server : private boost::noncopyable {

	public:
		explicit Server(std::size_t io_service_pool_size);
		void run();

	private:
		void start_accept();
		void handle_accept(const boost::system::error_code& e);
		void handle_stop();

		io_service_pool io_service_pool_;
		boost::asio::signal_set signals_;
		boost::asio::ip::tcp::acceptor acceptor_;
		connection_ptr new_connection_;
	};
}

#endif
