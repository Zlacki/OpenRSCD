#ifndef _CONNECTION_HPP
#define _CONNECTION_HPP

#include <boost/asio.hpp>
#include <boost/array.hpp>
#include <boost/noncopyable.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/enable_shared_from_this.hpp>

namespace RSC {
	namespace IO {
		/// Represents a single connection from a client.
		class connection
		  : public boost::enable_shared_from_this<connection>,
			private boost::noncopyable
		{
			public:
				/// Construct a connection with the given io_service.
				explicit connection(boost::asio::io_service& io_service);

				/// Get the socket associated with the connection.
				boost::asio::ip::tcp::socket& socket();

				/// Start the first asynchronous operation for the connection.
				void start();

				int getIndex();
				void setIndex(int);

				/// Handle completion of a write operation.
				void handle_write(char *, std::size_t);

			private:
				/// Handle completion of a read operation.
				void handle_read();

				/// Socket for the connection.
				boost::asio::ip::tcp::socket socket_;

				int index;
		};

		typedef boost::shared_ptr<connection> connection_ptr;
	}
}

#endif
